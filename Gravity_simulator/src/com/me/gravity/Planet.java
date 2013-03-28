package com.me.gravity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Planet {

	Vector Pos;
	Vector Vel;
	Vector Acel;
	Vector Force;
	
	double mass;
	double charge;
	double radii;
	
	double EPS = 0.00001f;
	
	double C = 0.2f;
	
	Sprite im;
	
	Arrow vel;
	Arrow acel;
	double F = 3;
	
	public Planet(double X, double Y, double Vx, double Vy ,double Charge, double Mass)
	{
			
		Pos = new Vector(X,Y);
		Vel = new Vector(Vx,Vy);
		Acel = new Vector(0,0);
		Force = new Vector(0,0);
		
		vel = new Arrow(Vel.x*F+Pos.x, Vel.y*F+Pos.y, Pos.x,Pos.y,1 );
		acel = new Arrow(0,0,0,0, 2);
		mass = Mass;
		charge = Charge;
		
		radii = (double)Math.sqrt(Math.abs(mass*C));
		
		im = new Sprite( Gravity.planet);
		im.setSize(( float)(2*radii), (float)(2*radii) );
		im.setOrigin(im.getWidth()/2, im.getHeight()/2);
		im.setPosition( (float)(Pos.x- im.getWidth()/2), (float)(Pos.y - im.getHeight()/2));
		
	}
	
	public void update(double dt)
	{
		if( Gravity.bounded)
		{
			if( Pos.x > Gravity.W/2-radii || Pos.x < -Gravity.W/2 + radii)
				Vel.x=-Vel.x;
			if( Pos.y  > Gravity.H/2 - radii || Pos.y  < -Gravity.H/2 + radii )
				Vel.y = -Vel.y;
		}
		Acel.x = Force.x/mass;
		Acel.y = Force.y/mass;
		Vel.x += Acel.x*dt;
		Vel.y += Acel.y*dt;
		
		vel.change(Vel.x*F + Pos.x , Vel.y*F + Pos.y , Pos.x , Pos.y);
		acel.change(Acel.x*F + Pos.x , Acel.y*F + Pos.y , Pos.x , Pos.y);
		
		Pos.x += Vel.x*dt;
		Pos.y += Vel.y*dt;
		
		Force.x = 0;
		Force.y = 0;
		
		im.setPosition( (float)(Pos.x - im.getWidth()/2), (float)(Pos.y  - im.getHeight()/2) );
	}
	
	public void addForce (double dx, double dy)
	{
		Force.x += dx;
		Force.y += dy;
	}
	
	public void draw(SpriteBatch b)
	{
		im.draw(b);
		if( Gravity.forces == true)
		{
			vel.draw(b);
			acel.draw(b);
		}
	}
	
	public void collision(Planet X)
	{
		Vector sep = new Vector( Pos.add( X.Pos.mult(-1) ) );
		if( sep.sizesq() <= (radii+X.radii)*(radii+X.radii) )
		{
			
			sep.multS ( (radii+X.radii + EPS)/sep.size() );
			Pos = X.Pos.add( sep );	
			
			Vector RelVelX = new Vector( X.Vel.add( Vel.mult(-1) ) );
			//System.out.println("RELVELX: " + RelVelX.x + " " + RelVelX.y + " " + RelVelX.size());
			sep.norm();
			//System.out.println("SEP: " + sep.x + " " + sep.y + " " + sep.size());
			sep.multS( sep.dot( RelVelX ) );
			//System.out.println("SEP: " + sep.x + " " + sep.y + " " + sep.size());
			
			Vector RelVelA = sep.mult( 2*X.mass/(mass+X.mass));
			//System.out.println("RelVelA: " + RelVelA.x + " " + RelVelA.y + " " + RelVelA.size());
			
			Vector RelVelXaf = sep.mult( (X.mass- mass)/(mass+X.mass) );
			//System.out.println("RelVelXaf: " + RelVelXaf.x + " " + RelVelXaf.y + " " + RelVelXaf.size());
			
			Vector K = RelVelXaf.add( sep.mult(-1) );
			K.multS(-1);
			
			//System.out.println("Kf: " + K.x + " " + K.y + " " + K.size());
			//System.out.println("Vel: " + Vel.x + " " + Vel.y + " " + Vel.size());
			//System.out.println("XVel: " + X.Vel.x + " " + X.Vel.y + " " + X.Vel.size());
			
			RelVelX.addS(K.mult(-1));
			RelVelX.addS( Vel);
			X.Vel = RelVelX;
			Vel.addS(RelVelA);
			
			//System.out.println("Vel: " + Vel.x + " " + Vel.y + " " + Vel.size());
			//System.out.println("XVel: " + X.Vel.x + " " + X.Vel.y + " " + X.Vel.size());
			

			//return true;
		}
		//return false;
	}
	
	public void addMass( double M)
	{
		mass+=M;
		radii = Math.sqrt( Math.abs(mass*C) );
		im.setSize( (float)(2*radii), (float)(2*radii));
		im.setPosition((float)(Pos.x - im.getWidth()/2), (float)(Pos.y  - im.getHeight()/2));
	}
	
}
