package middlewareVision.config;

import kmiddle2.util.IDHelper;
public class AreaNames {
    
    
        
        /****************************************************************************/
	public static int V1	=           IDHelper.generateID("V1", 0, 0);
        /****************************************************************************/
        public static int V1SimpleCells = IDHelper.generateID("V1", 1, 0);
	public static int V1ComplexCells = IDHelper.generateID("V1", 2, 0);
	public static int V1DoubleOpponent = IDHelper.generateID("V1", 3, 0);
	public static int V1HyperComplex = IDHelper.generateID("V1", 4, 0);
        public static int V1MotionCells = IDHelper.generateID("V1", 5 , 0);  
        public static int V1SimpleCellsFilter = IDHelper.generateID("V1", 7, 0);
        public static int V1MotionCells2 = IDHelper.generateID("V1", 8 , 0);
        
        /****************************************************************************/
        public static int V2	=           IDHelper.generateID("V2", 0, 0);
        /****************************************************************************/
	public static int V2AngularCells	=   IDHelper.generateID("V2",1,0);	
        public static int V2IlusoryCells = IDHelper.generateID("V2", 2 , 0);
        
        /****************************************************************************/
        public static int V4 =              IDHelper.generateID("V4",0,0);
        /****************************************************************************/
        public static int V4Color = IDHelper.generateID("V4", 2, 0);	
	public static int V4Contour = IDHelper.generateID("V4", 1, 0);
        public static int V4ShapeActivationNode = IDHelper.generateID("V4", 3 , 0);
        /****************************************************************************/
        public static int Retina =          IDHelper.generateID("Retina", 0, 0);
        /****************************************************************************/
        public static int RetinaProccess =  IDHelper.generateID("Retina", 1, 0);
        
	/****************************************************************************/
	public static int LGN = IDHelper.generateID("LGN", 0, 0);
        /****************************************************************************/
	public static int LGNProcess = IDHelper.generateID("LGN", 1, 0);
				
	
	public static int TestAttention = IDHelper.generateID("TestAttention", 0, 0);
	public static int FeedbackProccess = IDHelper.generateID("TestAttention", 1 , 0);
	
	public static int BasicMotion = IDHelper.generateID("Retina", 2 , 0);
	
	public static int ReichardtMotion = IDHelper.generateID("V1", 8 , 0);
	
	public static int V2CurvatureCells = IDHelper.generateID("V2", 3 , 0);
	
	//@addNodes
        //no quitar el comentario de add Nodes
}
