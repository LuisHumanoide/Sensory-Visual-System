package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.UnknownFormatConversionException;

/**
 *
 *
 * @param <T, L, I> L: data type for the Location I: data type for the Intensity
 * T: data type for the Timing Note that all the type parameters must be
 * Serializable
 *
 * A Spike-class that encapsulates the info passed in Cuayollotl-AC Modality: it
 * represents the type of the stimulus being coded (General type of data, like
 * Visual, AttnSet, etc) Location: it represents the location of the stimulus
 * (Specification of data, like a spatial-point, spectrum range, memory cluster,
 * etc). Timing: it represents the time that the stimulus intensity is present.
 */
public class LongSpike<  L extends Serializable, I extends Serializable, T extends Serializable> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5586813169286074998L;
    /**
     *
     */
    private int modality;
    private L location;
    private I intensity;
    private T timing;

    /**
     * Constructor*
     */
    /*
	 * @param 
	 * @return
	 * Requires a modality, the rest of the parameters are set to null.
     */
    public LongSpike(int modality) {
        this.modality = modality;
        this.location = null;
        this.intensity = null;
        this.timing = null;
    }

    public LongSpike(int modality, L location, I intensity, T timing) {
        this.modality = modality;
        this.location = location;
        this.intensity = intensity;
        this.timing = timing;
    }

    public LongSpike(byte[] spike) throws Exception {

        /* A byte[] spike has the first 10 bytes with the length of the 4 fields (in bytes):
		 * 2 bytes for the length of  Modality
		 * 2 bytes for the length of  Localization
		 * 4 bytes for the length of  Intensity
		 * 2 bytes for the length of Time
		 * The following bytes contain the data
         */
        int i = 0;
        byte[] tmpShort = new byte[2];
        byte[] tmpInt = new byte[4];

        //Retrieve lengths:
        System.arraycopy(spike, i, tmpShort, 0, 2);
        short mLength = shortFromByte(tmpShort);
        i += 2;
        System.arraycopy(spike, i, tmpShort, 0, 2);
        short lLength = shortFromByte(tmpShort);
        i += 2;
        System.arraycopy(spike, i, tmpInt, 0, 4);
        int iLength = intFromByte(tmpInt);
        i += 4;
        System.arraycopy(spike, i, tmpShort, 0, 2);
        short tLength = shortFromByte(tmpShort);
        i += 2;

        //Recover the data
        //Modality
        byte[] tmp = new byte[mLength];
        System.arraycopy(spike, i, tmp, 0, mLength);
        this.modality = intFromByte(tmp);
        i += mLength;

        //Location
        tmp = new byte[lLength];
        System.arraycopy(spike, i, tmp, 0, lLength);
        this.location = objectFromByte(tmp);
        i += lLength;

        //Intensity
        tmp = new byte[iLength];
        System.arraycopy(spike, i, tmp, 0, iLength);
        this.intensity = objectFromByte(tmp);
        i += iLength;

        //Timing
        tmp = new byte[tLength];
        System.arraycopy(spike, i, tmp, 0, tLength);
        this.timing = objectFromByte(tmp);
        i += tLength;

    }

    /**
     * Specific/static methods*
     */
    public byte[] getByteArray() throws IOException {
        /*
		 * First 10 bytes contain the length of the 4 fields (in bytes):
		 * 2 bytes for the length of  Modality
		 * 2 bytes for the length of  Localization
		 * 4 bytes for the length of  Intensity
		 * 2 bytes for the length of Time
		 * The following bytes contain the data
         */

        //Modality
        byte[] mByte = intToByte(this.modality);
        short mLength = (short) mByte.length;

        //Location
        byte[] lByte = objectToByte(this.location);
        short lLength = (short) lByte.length;

        //Intensity
        byte[] iByte = objectToByte(this.intensity);
        int iLength = (int) iByte.length;
        //System.out.println("Tama�o de la matriz al construir:" + iLength+" de modalidad: "+this.modality);

        //Timing
        byte[] tByte = objectToByte(this.timing);
        short tLength = (short) tByte.length;

        //Create full byte array	
        byte[] full = new byte[10 + mLength + lLength + iLength + tLength];

        //Fill full byte array
        //Start with the lengths
        int i = 0;
        System.arraycopy(shortToByte(mLength), 0, full, i, 2);
        i += 2;
        System.arraycopy(shortToByte(lLength), 0, full, i, 2);
        i += 2;
        System.arraycopy(intToByte(iLength), 0, full, i, 4);
        i += 4;
        System.arraycopy(shortToByte(tLength), 0, full, i, 2);
        i += 2;

        //Now the data
        System.arraycopy(mByte, 0, full, i, mLength);
        i += mLength;
        System.arraycopy(lByte, 0, full, i, lLength);
        i += lLength;
        System.arraycopy(iByte, 0, full, i, iLength);
        i += iLength;
        System.arraycopy(tByte, 0, full, i, tLength);

        return full;
    }

    /*
	 * Fast method to obtain just the Modality of a Spike
     */
    public static int getModality(byte[] spike) {
        byte[] tmp = new byte[4];
        System.arraycopy(spike, 10, tmp, 0, 4);
        ByteBuffer buffer = ByteBuffer.wrap(tmp);
        return buffer.getInt();
    }

    /*
	 * Fast method to obtain just the Location of a Spike.
	 * It needs the data type <M> of the Object Location
     */
    private <M> M getLocation(byte[] spike) throws IOException, ClassNotFoundException {
        byte[] tmp = new byte[2];
        System.arraycopy(spike, 2, tmp, 0, 2);
        ByteBuffer buffer = ByteBuffer.wrap(tmp);
        short length = buffer.getShort();

        tmp = new byte[length];
        System.arraycopy(spike, 14, tmp, 0, length);

        Object o = null;
        ByteArrayInputStream b = new ByteArrayInputStream(tmp);
        ObjectInput i = null;
        try {
            i = new ObjectInputStream(b);
            o = i.readObject();
        } finally {
            if (i != null) {
                i.close();
            }
        }
        return (M) o;
    }

    /**
     * Set-Get methods*
     */
    public void setModality(int modality) {
        this.modality = modality;
    }

    public void setLocation(L location) {
        this.location = location;
    }

    public void setIntensity(I intensity) {
        this.intensity = intensity;
    }

    public void setTiming(T timing) {
        this.timing = timing;
    }

    public int getModality() {
        return this.modality;
    }

    /*
	public String getModalityName(){
		return ModalityStrings.getModality(this.modality);
	}*/
    public L getLocation() {
        return this.location;
    }

    public I getIntensity() {
        return this.intensity;
    }

    public T getTiming() {
        return this.timing;
    }

    /**
     * Standard methods*
     */
    /*
	 * @param null
	 * @return Spike
	 * Returns an exact copy of this Spike
     */
    public LongSpike<L, I, T> clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean equals(LongSpike<L, I, T> spike) {
        try {
            if (spike.getModality() == this.modality && spike.getLocation().equals(this.location) && spike.getIntensity().equals(this.intensity) && spike.getTiming().equals(this.timing)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            throw new UnknownFormatConversionException("Unsoported data types");
        }
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
	public String toString(){
		try{
			return "Modality: " + ModalityStrings.getModality(modality) + 
				   "\nLocation: " + location.toString() + 
				   "\nIntensity: " + intensity.toString() + 
				   "\nTiming: " + timing.toString();
		}
		catch(Exception ex){
			throw new UnknownFormatConversionException("Unsoported data types");
		}
	}
     */
    private byte[] intToByte(int num) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(num);
        return buffer.array();
    }

    private <M> byte[] objectToByte(M obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = null;
        byte[] a = null;
        try {
            o = new ObjectOutputStream(b);
            o.writeObject(obj);
            o.flush();
            a = b.toByteArray();
        } catch (IOException e) {
            // ignore close exception
        } finally {
            b.close();
        }
        return a;
    }

    private byte[] shortToByte(short n) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(n);
        return buffer.array();
    }

    private int intFromByte(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }

    private short shortFromByte(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getShort();
    }

    private <M> M objectFromByte(byte[] bytes) throws IOException, ClassNotFoundException {
        Object o = null;
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInput i = null;
        try {
            i = new ObjectInputStream(b);
            o = i.readObject();
        } finally {
            if (i != null) {
                i.close();
            }
        }
        return (M) o;
    }
}
