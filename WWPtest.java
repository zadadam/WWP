/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wwptest;

//import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.pcj.*;
//import wwptest.WWPtest.Shared;

/**
 *
 * @author adam
 */
@RegisterStorage(WWPtest.Shared.class)
public class WWPtest  implements StartPoint {
    static String btsFile = "C:\\Users\\Marianna\\Desktop\\bts2.txt";
    static String bpFile = "C:\\Users\\Marianna\\Desktop\\bp_short.txt";
    static String terFile = "C:\\Users\\Marianna\\Desktop\\terytoria.txt"; 
    
    
    @Storage(WWPtest.class)
    enum Shared {
        adresy,
        terytorium,
        dlugosc,
        sciezkiZb,
        domy
    }

    ArrayList<dom> adresy;
    ArrayList<ArrayList<Punkt>> sciezkiZb;
    double dlugosc;
    int terytorium;
    int domy;
    
    /**
     *
     * @return
     */
    public static ArrayList<Integer> readTeryt() {
        ArrayList<Integer> t = new ArrayList<Integer>();
        Scanner sc = null;
        try {
            sc = new Scanner(new File(WWPtest.terFile));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            System.out.println("Error");
        }
        
        while (sc.hasNextLine()) {
            if(!sc.hasNextInt())
                break;
            int gmina = sc.nextInt();
            t.add(gmina);
        }
        return t;
    }
    
    public static ArrayList<BTS> readBTS() {
        ArrayList<BTS> listBts = new ArrayList<BTS>();
    
        Scanner sc = null;
        try {
            sc = new Scanner(new File(WWPtest.btsFile));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            System.out.println("Error");
        }
        
        while (sc.hasNextLine()) {
            if(!sc.hasNextInt())
                break;
            int gmina = sc.nextInt();
            //System.out.println(gmina);
            int terc = sc.nextInt();
            //System.out.println(terc);
            int typTerenu = sc.nextInt();
            //System.out.println(typTerenu);
            double lon = sc.nextDouble();
            double lat = sc.nextDouble();
            BTS b = new BTS(gmina, terc, typTerenu, lon, lat);
            listBts.add(b);
        }
        return listBts;
    }
    
    public static ArrayList<dom> readDom(int goodTerc) {
        ArrayList<dom> listDom = new ArrayList<dom>();
    
        Scanner sc = null;
        try {
            sc = new Scanner(new File(WWPtest.bpFile));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            System.out.println("Error");
        }
        
        while (sc.hasNextLine()) {
            if(!sc.hasNextInt())
                break;
            int terc = sc.nextInt();
            int gmina = sc.nextInt();
            int typGminy = sc.nextInt();
            int simc = sc.nextInt();
            int ulica = sc.nextInt();
            double lat = sc.nextDouble();
            double lon = sc.nextDouble();
            if(terc == goodTerc) {
                //System.out.println(terc);
                dom b = new dom(terc, gmina, typGminy, simc, ulica, lon, lat);
                listDom.add(b);
            }
        }
        return listDom;
    }
    
    public static Map readMapDom() {
        Map terytDom = new HashMap();
    
        Scanner sc = null;
        try {
            sc = new Scanner(new File(WWPtest.bpFile));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            System.out.println("Error");
        }
        
        while (sc.hasNextLine()) {
            if(!sc.hasNextInt())
                break;
            int terc = sc.nextInt();
            int gmina = sc.nextInt();
            int typGminy = sc.nextInt();
            int simc = sc.nextInt();
            int ulica = sc.nextInt();
            double lat = sc.nextDouble();
            double lon = sc.nextDouble();
            
            //System.out.println(terc);
            dom b = new dom(terc, gmina, typGminy, simc, ulica, lon, lat);
            
            if(terytDom.containsKey(terc)) {
                ((ArrayList<dom>) terytDom.get(terc)).add(b);
            } else {
                ArrayList<dom> l = new ArrayList<dom>();
                l.add(b);
                terytDom.put(terc, l);
            }
        }
        return terytDom;
    }
    
    public static double calcSwiatlowod(ArrayList<ArrayList<Punkt>> sciezki) {
        double l = 0.0;
        for(ArrayList<Punkt> s : sciezki) {
            for(int i = 1; i < s.size(); ++i) {
                l += s.get(i).distance(s.get(i-1));
            }
        }
        return l / 1000.0; // wynik w kilometrach
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String nodes[] = {"localhost","localhost","localhost","localhost","localhost","localhost","localhost","localhost"};
         //       PCJ.deploy(WWPtest.class, new NodesDescription("nodes.txt"));

        PCJ.deploy(WWPtest.class, new NodesDescription(nodes));
    }
    
    @Override
    public void main() {

        // zakaldamy, ze kazdym terytorium istnieje chociaz jeden BTS !!!
        
        int terc = 201011; // terytorium, ktore sprawdzamy
        ArrayList<BTS> listBts;
        ArrayList<dom> listDom;
        ArrayList<Integer> listTeryt;
        Map domyMap;
        
        listTeryt = readTeryt();
        
        terc = listTeryt.get(PCJ.myId());
        //System.out.println(terc);
        listBts = readBTS(); // kazdy dom sprawdza liste btsow tylko raz
        if(PCJ.myId() == 0) {
            domyMap = readMapDom();
            for(int i = 0; i < PCJ.threadCount(); ++i){
                int tercN = listTeryt.get(i);
                ArrayList<dom> dl = (ArrayList<dom>) domyMap.get(tercN);
                PCJ.put(dl, i, Shared.adresy);
                PCJ.put(tercN, i, Shared.terytorium);
            }
        }
        
        PCJ.barrier();
        listDom = adresy;
        domy = listDom.size();
        
        //System.out.println(listBts.size());
        //System.out.println(listDom.size());
        
        ArrayList<ArrayList<Punkt>> sciezki = new ArrayList<ArrayList<Punkt>>();
        
        Map domDist = new HashMap(); // lista odleglosci dom najbliszy BTS
        Map domBts = new HashMap(); // lista bts'ow najbliszych do domu

        // szukamy jaka jest najkrotsza droga do BTS'a
        for(dom d: listDom) {
            double distMin = 100000.0;
            for(BTS b: listBts) {
                double dist;
                dist = d.distance(b);
                if (dist < distMin) {
                    distMin = dist;
                    domDist.put(d, dist);
                    domBts.put(d, b);
                }
            }
        }
 
        if(listBts.isEmpty()) {
            System.out.println(PCJ.myId() + " " + terc + " " + "No BTS!");
            return;
        }
        
        // kazdy punkt rozwazamy tylko raz
        // albo podpinamy do BTS'a albo wysylamy do innego wezla
        for(dom d : listDom) {
            double distBest = (double) domDist.get(d);
            double bestSciezkaDist = 100000.0;
            boolean istniejeSciezka = false;
            int bestIndex = -1;
            
            
            for(int i = 0; i <  sciezki.size(); ++i) {
                double dist = -1.0;
                dom dd;
                dd = (dom) sciezki.get(i).get(sciezki.get(i).size()-1);
                dist = dd.distance(d);  // zupelnie jak java script
                //System.out.println(dist);
                //System.out.println(dd.lon + " " + dd.lat + " " +d.lon + " " + d.lat);
                //System.out.println(distFrom(dd.lon, dd.lat, d.lon, d.lat));
                // python lepszy
                if (dist < bestSciezkaDist) {
                    bestSciezkaDist= dist;
                }
                if (dist < distBest) {
                    istniejeSciezka = true;
                    distBest = dist;
                    bestIndex = i;
                }
            }
            
            //System.out.println(distBest + " " + bestSciezkaDist);
            if(istniejeSciezka) {
                sciezki.get(bestIndex).add(d);
            } else {
                ArrayList<Punkt> nowySwiatlowod = new ArrayList<Punkt>();
                nowySwiatlowod.add((BTS) domBts.get(d));
                nowySwiatlowod.add(d);
                sciezki.add(nowySwiatlowod);
            }
        }

        //for(ArrayList<Punkt> s : sciezki) {
        //    System.out.println(s.size());
        //}
        dlugosc = calcSwiatlowod(sciezki);
        sciezkiZb = sciezki;
        System.err.println("\t Skonczylem:" + PCJ.myId());
        PCJ.barrier();
        //System.out.println("\t" + PCJ.myId() + " " + terc + " " + calcSwiatlowod(sciezki) + " " + listDom.size());
        ArrayList<Integer> zT = new ArrayList<Integer>();
        ArrayList<Double> zDl = new ArrayList<Double>();
        ArrayList<ArrayList<ArrayList<Punkt>>> zSciezki = new ArrayList<ArrayList<ArrayList<Punkt>>>();
        ArrayList<Integer> zDomy = new ArrayList<Integer>();

        if(PCJ.myId() == 0) {
            for(int i = 0; i < PCJ.threadCount(); ++i){
                zT.add((Integer) PCJ.get(i, Shared.terytorium));
                zDl.add((Double) PCJ.get(i, Shared.dlugosc));
                zSciezki.add((ArrayList<ArrayList<Punkt>>) PCJ.get(i, Shared.sciezkiZb));
                zDomy.add((Integer) PCJ.get(i, Shared.domy));
                //PCJ.barrier();
            }
            for(int i = 0; i < PCJ.threadCount(); ++i) {
                System.err.println("--" +i + " " + zT.get(i) + " " + zDl.get(i) + " " + zDomy.get(i));
            }
        }
        
        //System.out.println(PCJ.myId() + " " + terc + " " + calcSwiatlowod(sciezki) + " " + listDom.size());
        // generowanie Wyniku
        if(PCJ.myId() == 0) {
            double calaDlugosc = 0.0;
            for(int i = 0; i < zDl.size(); ++i) {
                calaDlugosc += (Double) zDl.get(i);
            }
            System.err.println("++++ " + calaDlugosc + " ++++");
        }
        if(PCJ.myId() == 0) {
            int maxReg = 0;
            double dlugoscS = 0.0;
            for(int i = 0; i < PCJ.threadCount(); ++i) {
                dlugoscS += (Double) zDl.get(i);
                if(dlugoscS > 1000000.0)
                    break;
                maxReg = i;
            }
            maxReg = maxReg + 1;
        
            System.out.println(maxReg-1); // ilosc odcinkow
            for(int i = 0; i < maxReg; ++i) {
                int domy = (int) zDomy.get(i);
                int stacje = (int) zSciezki.get(i).size();
                int segmenty = (int) zSciezki.get(i).size();
                
                System.out.println(domy + " " + stacje + " " + odcinki);
                for(int j = 0; j < zSciezki.get(i).size(); ++j) {
                    for(int k = 1; k < zSciezki.get(i).get(j).size(); ++k){
                        dom d = (dom) zSciezki.get(i).get(j).get(k);
                        System.out.println(d.getLat() + " " +d.getLon());
                    }
                }
                
                // stacje - tu powinnismy usunac duplikaty - za malo czasu sorry
                for(int j = 0; j < zSciezki.get(i).size(); ++j) {
                    dom d = (dom) zSciezki.get(i).get(j).get(0);
                    System.out.println(d.getLat() + " " +d.getLon());                   
                }
                // odcinki
                for(int j = 0; j < zSciezki.get(i).size(); ++j) {
                    for(int k = 0; k < zSciezki.get(i).get(j).size(); ++k){
                        dom d = (dom) zSciezki.get(i).get(j).get(k);
                        System.out.print(d.getLat() + " " +d.getLon() + " ");
                    }
                    System.out.println();
                }
            }
            
        }
    }
    
    
}
