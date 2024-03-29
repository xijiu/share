package org.xijiu.share.board_game;

public class G extends Shape {

    private static final byte code = 6;

    private final int[][] origin1 = new int[][] {
            {1, 1, 1, 1, 1},
    };
    private final int[][] origin2 = new int[][] {
            {1},
            {1},
            {1},
            {1},
            {1},
    };

    private final int[] posArr1 = genericPosArr(origin1);
    private final int[] posArr2 = genericPosArr(origin2);

    public G(byte[][] board) {
        super(board);
    }


    @Override
    protected boolean tryPut(int i, int j) {
        boolean result;
        while (true) {
            switch (currForm) {
                case 1:
                    result = putToPos(code, posArr1, i, j);
                    break;
                case 2:
                    result = putToPos(code, posArr2, i, j);
                    break;
                default:
                    currForm = 1;
                    return false;
            }
            if (result) {
                break;
            }
        }
        return true;
    }

    @Override
    protected int getCode() {
        return code;
    }

}
