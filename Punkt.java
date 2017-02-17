/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wwptest;

import java.io.Serializable;

/**
 *
 * @author adam
 */
public abstract class Punkt implements Serializable {
    
    public abstract double distance(Punkt a);
    public abstract double getLon();
    public abstract double getLat();

}
