package spatialIndices;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import spatialObjects.PointOfInterest;
import spatialObjects.Subzone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UniformGrid implements Serializable {
    //private static final long serialVersionUID = 5162710183389028792L;
    static double minX;     //X - East-West longitude
    static double maxX;
    static double minY;     //Y - North-South latitude
    static double maxY;
    static int numGridPartitions;
    final static private int CELLINDEXSTRLENGTH = 5;
    private static final double mEarthRadius = 6371008.7714;
    HashSet<String> girdCellsSet = new HashSet<>();
    static double cellLength;

    public UniformGrid(double minX, double maxX, double minY, double maxY, int numGridPartitions) {
        UniformGrid.minX = minX;     //X - East-West longitude
        UniformGrid.maxX = maxX;
        UniformGrid.minY = minY;     //Y - North-South latitude
        UniformGrid.maxY = maxY;

        double xAxisDiff = maxX - minX;
        double yAxisDiff = maxY - minY;

        UniformGrid.numGridPartitions = numGridPartitions;

        cellLength = (UniformGrid.maxX - UniformGrid.minX) / numGridPartitions;

        // Adjusting coordinates to make square grid cells
        if(xAxisDiff > yAxisDiff)
        {
            double diff = xAxisDiff - yAxisDiff;
            maxY += diff / 2;
            minY -= diff / 2;
        }
        else if(yAxisDiff > xAxisDiff)
        {
            double diff = yAxisDiff - xAxisDiff;
            maxX += diff / 2;
            minX -= diff / 2;
        }
    }



    public double getMinX() {
        return minX;
    }
    public double getMaxX() {
        return maxX;
    }
    public double getMinY() {
        return minY;
    }
    public double getMaxY() {
        return maxY;
    }
    public int getNumGridPartitions() {
        return numGridPartitions;
    }

    public static double getCellLength() {
        return (maxX - minX) / numGridPartitions;
    }

    /*public double getCellLengthInMeters() {
        return computeHaverSine(minX, maxX, minY, maxY);
    }*/

    private static double computeHaverSine(double minX, double maxX, double minY, double maxY) {
        double rLat1 = Math.toRadians(minY);
        double rLat2 = Math.toRadians(maxY);
        double dLon=Math.toRadians(maxX-minX);
        return Math.acos(Math.sin(rLat1)*Math.sin(rLat2) + Math.cos(rLat1)*Math.cos(rLat2) * Math.cos(dLon)) * mEarthRadius;
    }

    public static HashSet<String> getGridCellsSet() {
        HashSet<String> gridCellsSet = new HashSet<>();
        for (int i = 0; i < numGridPartitions; i++) {
            for (int j = 0; j < numGridPartitions; j++) {
                try {
                    String cellKey = String.format("%0" + CELLINDEXSTRLENGTH + "d", i) + String.format("%0" + CELLINDEXSTRLENGTH + "d", j);
                    gridCellsSet.add(cellKey);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return gridCellsSet;
    }

    public static String getGridIndex(double x, double y) {
        if (minX < x && x<maxX && y>minY && y<maxY ) {
            int xCellIndex = (int) (Math.floor((x - minX) / getCellLength()));
            int yCellIndex = (int) (Math.floor((y - minY) / getCellLength()));
            return String.format("%0" + CELLINDEXSTRLENGTH + "d", xCellIndex) + String.format("%0" + CELLINDEXSTRLENGTH + "d", yCellIndex);
        } else {
            return null;
        }
    }

    public static String getGridIndex(Point p) {
        if (minX < p.getX() && p.getX()<maxX && p.getY()>minY && p.getY()<maxY ) {
            int xCellIndex = (int) (Math.floor((p.getX() - minX) / getCellLength()));
            int yCellIndex = (int) (Math.floor((p.getY() - minY) / getCellLength()));
            return String.format("%0" + CELLINDEXSTRLENGTH + "d", xCellIndex) + String.format("%0" + CELLINDEXSTRLENGTH + "d", yCellIndex);
        } else {
            return null;
        }
    }

    public static HashSet<String> getGridIndex(Polygon poly) {
        HashSet<String> gridCellIDs = new HashSet<>();
        Envelope bBox = poly.getEnvelopeInternal();
        double polyMinX = bBox.getMinX();
        double polyMaxX = bBox.getMaxX();
        double polyMinY = bBox.getMinY();
        double polyMaxY = bBox.getMaxY();

        // bottom-left coordinate (min values)
        int xCellIndex1 = (int) (Math.floor((polyMinX - minX) / getCellLength()));
        int yCellIndex1 = (int) (Math.floor((polyMinY - minY) / getCellLength()));

        // top-right coordinate (max values)
        int xCellIndex2 = (int) (Math.floor((polyMaxX - minX) / getCellLength()));
        int yCellIndex2 = (int) (Math.floor((polyMaxY - minY) / getCellLength()));

        if (minX < polyMinX && polyMaxX<maxX && polyMinY>minY && polyMaxY<maxY ) {
            for(int x = xCellIndex1; x <= xCellIndex2; x++)
                for(int y = yCellIndex1; y <= yCellIndex2; y++)
                {
                    String gridID = String.format("%0" + CELLINDEXSTRLENGTH + "d", x) + String.format("%0" + CELLINDEXSTRLENGTH + "d", y);
                    gridCellIDs.add(gridID);
                }
            return gridCellIDs;
        } else {
            return null;
        }
    }

    public static HashSet<String> getGridIndex(Subzone subzone) {
        return UniformGrid.getGridIndex((Polygon) (subzone.getGeom()));
    }

    public static String getGridIndex(PointOfInterest poi) {
        return UniformGrid.getGridIndex((Point) (poi.getGeom()));
    }


    public static HashSet<String> getGridIndex(LineString l) {
        HashSet<String> gridCellIDs = new HashSet<>();
        Envelope bBox = l.getEnvelopeInternal();
        double polyMinX = bBox.getMinX();
        double polyMaxX = bBox.getMaxX();
        double polyMinY = bBox.getMinY();
        double polyMaxY = bBox.getMaxY();

        // bottom-left coordinate (min values)
        int xCellIndex1 = (int) (Math.floor((polyMinX - minX) / getCellLength()));
        int yCellIndex1 = (int) (Math.floor((polyMinY - minY) / getCellLength()));

        // top-right coordinate (max values)
        int xCellIndex2 = (int) (Math.floor((polyMaxX - minX) / getCellLength()));
        int yCellIndex2 = (int) (Math.floor((polyMaxY - minY) / getCellLength()));

        if (minX < polyMinX && polyMaxX<maxX && polyMinY>minY && polyMaxY<maxY ) {
            for(int x = xCellIndex1; x <= xCellIndex2; x++)
                for(int y = yCellIndex1; y <= yCellIndex2; y++)
                {
                    String gridID = String.format("%0" + CELLINDEXSTRLENGTH + "d", x) + String.format("%0" + CELLINDEXSTRLENGTH + "d", y);
                    gridCellIDs.add(gridID);
                }
            return gridCellIDs;
        } else {
            return null;
        }
    }

    public static boolean validKey(int x, int y){
        if(x >= 0 && y >= 0 && x < numGridPartitions && y < numGridPartitions)
        {return true;}
        else
        {return false;}
    }


    /*
    getGuaranteedNeighboringCells: returns the cells containing the guaranteed r-neighbors
    getCandidateNeighboringCells: returns the cells containing the candidate r-neighbors and require distance computation
    The output set of the above two functions are mutually exclusive
    */

    public static HashSet<String> getGuaranteedNeighboringCells(double queryRadius, String queryGridCellID)
    {
        HashSet<String> guaranteedNeighboringCellsSet = new HashSet<String>();
        int guaranteedNeighboringLayers = getGuaranteedNeighboringLayers(queryRadius);

        // if guaranteedNeighboringLayers == -1, there is no GuaranteedNeighboringCells
        if(guaranteedNeighboringLayers == 0)
        {
            guaranteedNeighboringCellsSet.add(queryGridCellID);
        }
        else if(guaranteedNeighboringLayers > 0)
        {
            ArrayList<Integer> queryCellIndices = getIntCellIndices(queryGridCellID);       //converts cellID String->Integer

            for(int i = queryCellIndices.get(0) - guaranteedNeighboringLayers; i <= queryCellIndices.get(0) + guaranteedNeighboringLayers; i++)
                for(int j = queryCellIndices.get(1) - guaranteedNeighboringLayers; j <= queryCellIndices.get(1) + guaranteedNeighboringLayers; j++)
                {
                    if(validKey(i,j))
                    {
                        String neighboringCellKey = String.format("%0"+ CELLINDEXSTRLENGTH +"d", i) + String.format("%0"+ CELLINDEXSTRLENGTH +"d", j);
                        guaranteedNeighboringCellsSet.add(neighboringCellKey);
                    }
                }
        }
        return guaranteedNeighboringCellsSet;
    }

    public static HashSet<String> getCandidateNeighboringCells(double queryRadius, String queryGridCellID, Set<String> guaranteedNeighboringCellsSet)
    {
        // queryRadius = CoordinatesConversion.metersToDD(queryRadius,cellLength,cellLengthMeters);  //UNCOMMENT FOR HAVERSINE (METERS)
        //String queryCellID = queryPoint.gridID;
        HashSet<String> candidateNeighboringCellsSet = new HashSet<>();
        int candidateNeighboringLayers = getCandidateNeighboringLayers(queryRadius);

        if(candidateNeighboringLayers > 0)
        {
            ArrayList<Integer> queryCellIndices = getIntCellIndices(queryGridCellID);
            //int count = 0;

            for(int i = queryCellIndices.get(0) - candidateNeighboringLayers; i <= queryCellIndices.get(0) + candidateNeighboringLayers; i++)
                for(int j = queryCellIndices.get(1) - candidateNeighboringLayers; j <= queryCellIndices.get(1) + candidateNeighboringLayers; j++)
                {
                    if(validKey(i,j)) {
                        String neighboringCellKey = String.format("%0"+ CELLINDEXSTRLENGTH +"d", i) + String.format("%0"+ CELLINDEXSTRLENGTH +"d", j);
                        if (!guaranteedNeighboringCellsSet.contains(neighboringCellKey)) // Add key if and only if it exist in the gridCell and is not included in the guaranteed neighbors
                        {
                            candidateNeighboringCellsSet.add(neighboringCellKey);
                        }
                    }
                }
        }
        return candidateNeighboringCellsSet;
    }

    private static int getCandidateNeighboringLayers(double queryRadius) {
        return (int)Math.ceil(queryRadius/cellLength);
    }


    private static ArrayList<Integer> getIntCellIndices(String cellID) {
        ArrayList<Integer> cellIndices = new ArrayList<>();

        String cellIDX = cellID.substring(0,5);
        String cellIDY = cellID.substring(5);

        cellIndices.add(Integer.parseInt(cellIDX.replaceFirst("^0+(?!$)", "")));
        cellIndices.add(Integer.parseInt(cellIDY.replaceFirst("^0+(?!$)", "")));

        return cellIndices;
    }

    public static int getGuaranteedNeighboringLayers(double queryRadius)
    {
        double cellDiagonal = cellLength*Math.sqrt(2);
        return (int)Math.floor((queryRadius/cellDiagonal) - 1);
        // If return value = -1 then not even the cell containing the query is guaranteed to contain r-neighbors
        // If return value = 0 then only the cell containing the query is guaranteed to contain r-neighbors
        // If return value is a positive integer n, then the n layers around the cell containing the query is guaranteed to contain r-neighbors
    }

}
