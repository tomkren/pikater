package org.pikater.shared.visualisation.renderer;

import java.awt.Color;

public interface RendererInterface {
	
	public enum TextAlignment{
		Left,
		Center,
		Right
	};
	
	public void begin();
	public void end();
	
	public void drawCircle(int x, int y, int radius);
	public void drawCircle(int x, int y, int radius,Color stroke,Color fill,int strokeWidth);
	public void drawText(String text,int x,int y,TextAlignment textAlignment,Color color);
	public void drawText(String text,int x,int y,TextAlignment textAlignment,Color color,int rotationDegree);
	public void drawText(String text,int x,int y,TextAlignment textAlignment,Color color,int rotationDegree,int size);
	public void drawText(String text,int x,int y,TextAlignment textAlignment,Color color,int rotationDegree,int rotationX,int rotationY);
	public void drawText(String text,int x,int y,TextAlignment textAlignment,Color color,int rotationDegree,int rotationX,int rotationY,int size);
	public void drawLine(int x1,int y1,int x2,int y2,Color color,int strokeWidth);
	public void drawRectangle(int x,int y,int width,int height,Color fillColor,Color strokeColor,int strokeWidth);
}
