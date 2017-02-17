/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wwptest;

/**
 *
 * @author adam
 */
public class dom extends Punkt{
    public int idDom;
    public int terc;
    public int gmina;
    public int typGminy;
    public int simc; // co to do cholery jest
    public int ulica;
    public double lon;
    public double lat;

    public dom(int terc, int gmina, int typGminy, int simc, int ulica, double lon, double lat) {
        this.gmina = gmina;
        this.terc = terc;
        this.simc = simc;
        this.typGminy = typGminy;
        this.ulica = ulica;
        this.lon = lon;
        this.lat = lat;
    }
    
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        // wziete z 
        // http://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = (double) (earthRadius * c);

        return dist;
    }
    
    @Override
    public double distance(Punkt a) {
        return distFrom(this.lat, this.lon, a.getLat(), a.getLon());
    }

    @Override
    public double getLon() {
        return this.lon;
    }

    @Override
    public double getLat() {
        return this.lat;
    }
}
