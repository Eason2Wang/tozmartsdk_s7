package com.tozmart.tozisdk.view.editprofileview;

import android.graphics.PointF;

import com.tozmart.tozisdk.entity.ProfilePaintLine;

import java.util.ArrayList;

public class Step {
    private ArrayList<PointF> drawPoints = new ArrayList<>();
    private ArrayList<MovePanObject> movePans = new ArrayList<>();
    private MovePanObject extraPan;
    ArrayList<ProfilePaintLine> drawPaintLines = new ArrayList<>();
    ArrayList<MoveRectObject> moveRectObjects = new ArrayList<>();
    ArrayList<MoveFreeLineObject> moveFreeLineObjects = new ArrayList<>();
    private CurrentMode currentmode;

    public Step(ArrayList<PointF> drawPoints, ArrayList<MovePanObject> movePans, CurrentMode currentmode) {
        this.drawPoints.addAll(drawPoints);
        for (MovePanObject object : movePans) {
            this.movePans.add(object.clone());
        }
        this.currentmode = currentmode;
    }

    public Step(ArrayList<PointF> drawPoints, ArrayList<MovePanObject> movePans,
                MovePanObject extraPan, CurrentMode currentmode) {
        this.drawPoints.clear();
        this.drawPoints.addAll(drawPoints);
        this.movePans.clear();
        for (MovePanObject object : movePans) {
            this.movePans.add(object.clone());
        }
        this.drawPaintLines.clear();
        for (ProfilePaintLine paintLine : drawPaintLines) {
            this.drawPaintLines.add(paintLine.copy());
        }
        this.moveRectObjects.clear();
        for (MoveRectObject object : moveRectObjects) {
            this.moveRectObjects.add(object.clone());
        }
        this.moveFreeLineObjects.clear();
        for (MoveFreeLineObject object : moveFreeLineObjects) {
            this.moveFreeLineObjects.add(object.clone());
        }
        this.currentmode = currentmode;
        this.extraPan = extraPan;
    }

    public Step(ArrayList<PointF> drawPoints, ArrayList<MovePanObject> movePans,
                ArrayList<ProfilePaintLine> drawPaintLines,
                ArrayList<MoveRectObject> moveRectObjects,
                ArrayList<MoveFreeLineObject> moveFreeLineObjects,
                CurrentMode currentmode) {
        this.drawPoints.clear();
        this.drawPoints.addAll(drawPoints);
        this.movePans.clear();
        for (MovePanObject object : movePans) {
            this.movePans.add(object.clone());
        }
        this.drawPaintLines.clear();
        for (ProfilePaintLine paintLine : drawPaintLines) {
            this.drawPaintLines.add(paintLine.copy());
        }
        this.moveRectObjects.clear();
        for (MoveRectObject object : moveRectObjects) {
            this.moveRectObjects.add(object.clone());
        }
        this.moveFreeLineObjects.clear();
        for (MoveFreeLineObject object : moveFreeLineObjects) {
            this.moveFreeLineObjects.add(object.clone());
        }
        this.currentmode = currentmode;
    }

    public ArrayList<MovePanObject> getMovePans() {
        return movePans;
    }

    public void setMovePans(ArrayList<MovePanObject> movePans) {
        this.movePans = movePans;
    }

    public ArrayList<PointF> getDrawPoints() {

        return drawPoints;
    }

    public void setDrawPoints(ArrayList<PointF> drawPoints) {
        this.drawPoints = drawPoints;
    }

    public CurrentMode getCurrentmode() {
        return currentmode;
    }

    public void setCurrentmode(CurrentMode currentmode) {
        this.currentmode = currentmode;
    }

    public MovePanObject getExtraPan() {
        return extraPan;
    }

    public void setExtraPan(MovePanObject extraPan) {
        this.extraPan = extraPan;
    }

    public ArrayList<MoveRectObject> getMoveRectObjects() {
        return moveRectObjects;
    }

    public void setMoveRectObjects(ArrayList<MoveRectObject> moveRectObjects) {
        this.moveRectObjects = moveRectObjects;
    }

    public ArrayList<MoveFreeLineObject> getMoveFreeLineObjects() {
        return moveFreeLineObjects;
    }

    public void setMoveFreeLineObjects(ArrayList<MoveFreeLineObject> moveFreeLineObjects) {
        this.moveFreeLineObjects = moveFreeLineObjects;
    }

    public ArrayList<ProfilePaintLine> getDrawPaintLines() {
        return drawPaintLines;
    }

    public void setDrawPaintLines(ArrayList<ProfilePaintLine> drawPaintLines) {
        this.drawPaintLines = drawPaintLines;
    }
}
