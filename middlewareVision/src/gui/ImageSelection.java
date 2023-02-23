/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author HumanoideFilms
 */
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Implementación de Transferable para imagenes.
 * Este dato ImageSelection implementa Transferable y contiene la imagen.
 * Es el dato que se debe meter en el Clipboard.
 * @author Chuidiang
 *
 */
public class ImageSelection implements Transferable {
	private Image image;

	/**
	 * Se guarda la imagen que se le pasa.
	 * @param image
	 */
	public ImageSelection(Image image) {
		this.image = image;
                
	}

	/**
	 * Devuelve los DataFlavor soportados, es decir,
	 * DataFlavor.imageFlavor
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DataFlavor.imageFlavor };
	}

	/**
	 * Devuelve true si el flavor que se le pasa es DataFlavor.imageFlavor
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return DataFlavor.imageFlavor.equals(flavor);
	}

	/**
	 * Devuelve la imagen si el DataFlavor que se le pasa es DataFlavor.imageFlavor
	 */
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (!DataFlavor.imageFlavor.equals(flavor)) {
			throw new UnsupportedFlavorException(flavor);
		}
		return image;
	}
}