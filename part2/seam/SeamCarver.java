/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.util.ArrayList;
import java.util.List;

public class SeamCarver {
    private int W;
    private int H;
    private Picture image;
    private double[][] energies;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Picture is null!");
        W = picture.width();
        H = picture.height();
        image = new Picture(picture);
        energies = new double[W][H];
        computeEnergies();
    }

    // current picture
    public Picture picture() {
        return new Picture(image);
    }

    // width of current picture
    public int width() {
        return W;
    }

    // height of current picture
    public int height() {
        return H;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if ((x < 0) || (x > W - 1) || (y < 0) || (y > H - 1))
            throw new IllegalArgumentException("Pixels outside of range!");
        return energies[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = new int[W];
        int[][] edgeTo = new int[W][H];
        double[][] distTo = new double[W][H];

        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                edgeTo[i][j] = -1;
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int j = 0; j < H; j++) {
            edgeTo[0][j] = j;
            distTo[0][j] = 1000;
        }

        for (int i = 0; i < W - 1; i++) {
            for (int j = 0; j < H; j++) {
                List<Integer[]> neighbors = new ArrayList<Integer[]>();
                neighbors.add(new Integer[] { i + 1, j });
                if (j > 0) neighbors.add(new Integer[] { i + 1, j - 1 });
                if (j < H - 1) neighbors.add(new Integer[] { i + 1, j + 1 });
                for (Integer[] neighbor : neighbors) {
                    int newI = neighbor[0];
                    int newJ = neighbor[1];
                    if (distTo[i][j] + energies[newI][newJ] <= distTo[newI][newJ]) {
                        distTo[newI][newJ] = distTo[i][j] + energies[newI][newJ];
                        edgeTo[newI][newJ] = j;
                    }
                }
            }
        }

        int minJ = 0;
        double minDistance = distTo[W - 1][0];
        for (int j = 1; j < H; j++) {
            if (distTo[W - 1][j] < minDistance) {
                minJ = j;
                minDistance = distTo[W - 1][j];
            }
        }

        double checkEnergy = energies[W - 1][minJ];
        seam[W - 1] = minJ;
        for (int i = W - 2; i >= 0; i--) {
            seam[i] = edgeTo[i + 1][seam[i + 1]];
            checkEnergy += energies[i][seam[i]];
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[H];
        int[][] edgeTo = new int[W][H];
        double[][] distTo = new double[W][H];

        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                edgeTo[i][j] = -1;
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int i = 0; i < W; i++) {
            edgeTo[i][0] = i;
            distTo[i][0] = 1000;
        }

        for (int j = 0; j < H - 1; j++) {
            for (int i = 0; i < W; i++) {
                List<Integer[]> neighbors = new ArrayList<Integer[]>();
                neighbors.add(new Integer[] { i, j + 1 });
                if (i > 0) neighbors.add(new Integer[] { i - 1, j + 1 });
                if (i < W - 1) neighbors.add(new Integer[] { i + 1, j + 1 });
                for (Integer[] neighbor : neighbors) {
                    int newI = neighbor[0];
                    int newJ = neighbor[1];
                    if (distTo[i][j] + energies[newI][newJ] <= distTo[newI][newJ]) {
                        distTo[newI][newJ] = distTo[i][j] + energies[newI][newJ];
                        edgeTo[newI][newJ] = i;
                    }
                }
            }
        }

        int minI = 0;
        double minDistance = distTo[0][H - 1];
        for (int i = 1; i < W; i++) {
            if (distTo[i][H - 1] < minDistance) {
                minI = i;
                minDistance = distTo[i][H - 1];
            }
        }

        double checkEnergy = energies[minI][H - 1];
        seam[H - 1] = minI;
        for (int j = H - 2; j >= 0; j--) {
            seam[j] = edgeTo[seam[j + 1]][j + 1];
            checkEnergy += energies[seam[j]][j];
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!validSeam(seam, false)) throw new IllegalArgumentException("Seam is not valid");
        Picture updatedImage = new Picture(W, H - 1);
        int updatedJ = 0;
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                if (j == seam[i]) continue;
                updatedImage.set(i, updatedJ, image.get(i, j));
                updatedJ += 1;
            }
            updatedJ = 0;
        }
        image = updatedImage;
        H -= 1;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (!validSeam(seam, true)) throw new IllegalArgumentException("Seam is not valid");
        Picture updatedImage = new Picture(W - 1, H);
        int updatedI = 0;
        for (int j = 0; j < H; j++) {
            for (int i = 0; i < W; i++) {
                if (i == seam[j]) continue;
                updatedImage.set(updatedI, j, image.get(i, j));
                updatedI += 1;
            }
            updatedI = 0;
        }
        image = updatedImage;
        W -= 1;
    }

    // compute energies for the whole picture
    private void computeEnergies() {
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                energies[i][j] = computeEnergies(i, j);
            }
        }
    }

    // compute energies for single pixel
    private double computeEnergies(int x, int y) {
        if ((x == 0) || (y == 0) || (x == W - 1) || (y == H - 1)) return 1000;
        double gradientXsquared = computeGradientSquared(x, y, true);
        double gradientYsquared = computeGradientSquared(x, y, false);
        return Math.sqrt(gradientXsquared + gradientYsquared);
    }

    // compute squared gradient for each point
    private double computeGradientSquared(int x, int y, boolean isX) {
        int point1x, point1y, point2x, point2y;
        if (isX) {
            point1x = x - 1;
            point1y = y;
            point2x = x + 1;
            point2y = y;
        }
        else {
            point1x = x;
            point1y = y - 1;
            point2x = x;
            point2y = y + 1;
        }
        int color1 = image.getRGB(point1x, point1y);
        int color2 = image.getRGB(point2x, point2y);
        int r = getRed(color1) - getRed(color2);
        int g = getGreen(color1) - getGreen(color2);
        int b = getBlue(color1) - getBlue(color2);
        return r * r + g * g + b * b;
    }

    // get red from 32 bit color
    private int getRed(int color) {
        return (color >> 16) & 0xFF;
    }

    // get blue from 32 bit color
    private int getBlue(int color) {
        return (color >> 8) & 0xFF;
    }

    // get green from 32 bit color
    private int getGreen(int color) {
        return color & 0xFF;
    }

    // check seam validity
    private boolean validSeam(int[] seam, boolean vertical) {
        if (seam == null) return false;
        int expectedLength = (vertical) ? H : W;
        int margin = (vertical) ? W : H;
        if (margin <= 1) return false;
        if (seam.length != expectedLength) return false;
        for (int i = 0; i < seam.length; i++) {
            if ((seam[i] < 0) || (seam[i] > margin - 1)) return false;
            if (i == seam.length - 1) return true;
            if ((seam[i] - seam[i + 1] > 1) || (seam[i] - seam[i + 1] < -1)) return false;
        }
        return true;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture pic = new Picture(args[0]);
        SeamCarver seam = new SeamCarver(pic);
        System.out.println(seam.width());
        System.out.println(seam.height());
        for (int i = 0; i < seam.width(); i++) {
            for (int j = 0; j < seam.height(); j++) {
                System.out.print(seam.energy(i, j) + " ");
            }
            System.out.println();
        }
        int[] minSeam = seam.findHorizontalSeam();
        for (int i = 0; i < minSeam.length; i++) System.out.print(minSeam[i] + " ");
        System.out.println();
        minSeam = seam.findVerticalSeam();
        for (int i = 0; i < minSeam.length; i++) System.out.print(minSeam[i] + " ");
        System.out.println();
    }
}
