package hk.edu.polyu.comp.comp2021.assignment1.shape;

public class XYRectangle {
    private Point topleft;
    private Point bottomright;

    public Point getTopLeft() {
        return topleft;
    }

    public Point getBottomRight() {
        return bottomright;
    }

    public XYRectangle(Point p1, Point p2) {
        // Todo: complete the constructor
        this.topleft = p1;
        this.bottomright = p2;
    }

    public String toString() {
        // Todo: complete the method
        return "<" + topleft.toString() + "," + bottomright.toString() + ">";
    }

    public int area() {
        // Todo: complete the method
        int length = Math.abs(bottomright.getX()) - topleft.getX();
        int width = Math.abs(topleft.getY() - bottomright.getY());
        int area = length * width;
        return area;
    }

    public void rotateClockwise() {
        // Todo: complete the method
        int length = Math.abs(bottomright.getX()) - topleft.getX();
        int width = Math.abs(topleft.getY() - bottomright.getY());
        ;
        int Newtlpl = topleft.getX() - width;
        int Newtlp2 = topleft.getY();
        int Newbrp1 = topleft.getX();
        int Newbrp2 = topleft.getY() - length;
        topleft = new Point(Newtlpl, Newtlp2);
        bottomright = new Point(Newbrp1, Newbrp2);
    }

    public void move(int deltaX, int deltaY) {
        // Todo: complete the method
        int Newtlpl = topleft.getX() + deltaX;
        int Newtlp2 = topleft.getY() + deltaY;
        int Newbrp1 = bottomright.getX() + deltaX;
        int Newbrp2 = bottomright.getY() + deltaY;
        topleft = new Point(Newtlpl, Newtlp2);
        bottomright = new Point(Newbrp1, Newbrp2);
    }

    public boolean contains(Point p) {
        // Todo: complete the method
        int x = p.getX();
        int y = p.getY();
        int topleftx = topleft.getX();
        int bottomrightx = bottomright.getX();
        int toplefty = topleft.getY();
        int bottomrighty = bottomright.getY();
        if (topleftx <= x && x <= bottomrightx&&bottomrighty <= y && y <= toplefty) {
            return true;
        }
            else {
                return false;
            }
        }



    public boolean contains(XYRectangle r){
        // Todo: complete the method
        Point x=r.topleft;
        Point y=r.bottomright;
        if (contains(x)&&contains(y)) {
            return true;
        }
        else {
            return false;
        }
    }


    public boolean overlapsWith(XYRectangle r){
        // Todo: complete the method
        Point x=r.topleft;
        Point y=r.bottomright;
        if (contains(x)||contains(y)||r.contains(this.bottomright)||r.contains(this.topleft)) {
            return true;
        }
        else {
            return false;
        }
    }
}

class Point{
    private int x;
    private int y;

    public Point(int x, int y) {
        set(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + getX() + "," + getY() + ")";
    }
}

