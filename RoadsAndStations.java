import javax.swing.*;
import java.io.*;
import java.util.*;

class MainClass {
	public static void main(String[] args){
	program:{								//if an error occur this block is broken and the program stops
String[] a=new String [3];
if(args.length==3){a=args;}
else if(args.length==2){a[0]=args[0];a[1]=args[1];a[2]="0";}
else if(args.length==1){a[0]=args[0];a[1]="0";a[2]="0";}
else {a[0]="Example.txt";a[1]="0";a[2]="0";}
//getting the map file
	String dataFileName=JOptionPane.showInputDialog("Enter file name:",a[0]);
	Reader file = new Reader(dataFileName);
	if(!file.openFile()){JOptionPane.showMessageDialog(null,"File not found!","Alert",JOptionPane.PLAIN_MESSAGE);break program;}
	//getting departure and arrival stations id's
	String fn = JOptionPane.showInputDialog("Enter departure station id:",a[1]);
	String sn = JOptionPane.showInputDialog("Enter arrival station id:",a[2]);
	int src=Integer.parseInt(fn);
	int des=Integer.parseInt(sn);
	//reading the map file
	List<String> read = new ArrayList<String>();
	read=file.readFile();
	int l=read.size();
	String[] x=new String[l];
	int i=0;
	//next save the map into a two dimensional array of strings
	for(String line: read)
	{
	x[i]=line;
	i++;
	}

	String[] lengthTest = new String[l];
	int max=0;
	
	for(i=0;i<l;i++){
		lengthTest=x[i].split("\\s",0);
		if(lengthTest.length>max){max=lengthTest.length;}
	}
	
	file.closeFile();
	String[][] city=new String[l][max];
	String[] temp=new String[max];
	int k,j;
	boolean wrongMap=false;
	
	for(j=0;j<l;j++){
		temp=x[j].split("\\s",0);
		
		for(k=0;k<max;k++){
			try{
				city[j][k]=temp[k];
				if(Integer.parseInt(city[j][k])<1){wrongMap=true;}
			}catch(Exception e){
				city[j][k]="0";
			}
			
		}
	}
	//handling the error of entering a departure or arrival station with id zero
	//because the program is instructed to fill the gaps in the created two dimensional array with zero id's
	boolean s=false;
	boolean d=false;
	if(src<1||des<1){JOptionPane.showMessageDialog(null,"Invalid input!\nStations ids should not be less than one!","Alert",JOptionPane.PLAIN_MESSAGE);break program;}
	if(wrongMap){JOptionPane.showMessageDialog(null,"Invalid map!\nStations ids should not be less than one!","Alert",JOptionPane.PLAIN_MESSAGE);break program;}
	//handling the error of entering a departure or arrival station id that doesnt exist in the map file
	for(j=0;j<l;j++){		
		for(k=1;k<max;k++){
			if(city[j][k].equals(fn)){s=true;}
			if(city[j][k].equals(sn)){d=true;}
		}
	}
	
	if(!s){JOptionPane.showMessageDialog(null,"Departure station does not exist!","Alert",JOptionPane.PLAIN_MESSAGE);break program;}
	if(!d){JOptionPane.showMessageDialog(null,"Arrival station does not exist!","Alert",JOptionPane.PLAIN_MESSAGE);break program;}
	//using methods created on other classes to get the answer in the form of string or array of strings of the routes id's
	int t;
	Indirect I=new Indirect();
	String SIN,IN;
	
	Direct D= new Direct();
	String ds=D.direct(city, l, max, src, des);
	String dss=D.direct(city, l, max, src, des);

	//this block is for creating an appropriate string to be printed as the result 
	//the block is broken if there is no indirect route found
	ind:{
	String[] in= I.indirect(city, l, max, fn, sn);
	if(in[0].equals("0")){IN="[ 0 ]";SIN="[ 0 ]";break ind;}
	IN="[ "+in[0]+" , ";
	for(t=1;t<in.length-1;t++){IN=IN+in[t]+" , ";}
	IN=IN+in[in.length-1]+" ]";
	
	String[] sin= I.indirectShort(city, l, max, fn, sn);
	SIN="[ "+sin[0]+" , ";
	for(t = 1;t<sin.length-1;t++){SIN=SIN+sin[t]+" , ";}
	SIN=SIN+sin[sin.length-1]+" ]";}
	//printing the answer in a GUI form for clearance
	JOptionPane.showMessageDialog(null,
			"Direct route: "+
			ds+
			"\nShortest direct route: "+
			dss
			+"\nIndirect route: "+
			IN+
			"\nShortest indirect route: "+
			SIN,
			"Answer", JOptionPane.PLAIN_MESSAGE);
	//printing the developer info just in case it was lost or something
	JOptionPane.showMessageDialog(null,"Made by: \nName: Yazeed Mohi-Eldeen\nIndex: 145096\n//Just in case","Developer",JOptionPane.PLAIN_MESSAGE);
	}}
}







class Reader {
	
	
	
	public Reader(String Name){
		this.FileName=Name;
	}
	
	
	
	
	private String FileName;
	private Scanner x;
	
	
	
	//if the file was not found the error is handled and a message is printed to the user in a GUI form
	public boolean openFile(){
		try{
			x = new Scanner(new File(FileName));
		}catch(Exception e){
			return false;
		}
		return true;
	}
	

	
	
	public List<String> readFile(){
		List<String> y =new ArrayList<String>();
		while(x.hasNextLine()){
		y.add(x.nextLine());
		
		}

		return y;
	}
	
	
	
	
	public void closeFile(){
		x.close();
	}
}








class Direct {
	public String direct(String[][] c,int x,int y,int src,int dest){
	//some important global variables
	String s=Integer.toString(src);
	String d=Integer.toString(dest);
	int m1=0;
	int m2=0;
	String result="0";
	//a block to be broken if the result was found
	loop:{for(int i=0;i<x;i++){
		for(int j=1;j<y;j++){
			if(c[i][j].equals(s)){
				m1=1;
			}
			if(c[i][j].equals(d)){
				m2=1;
			}
			if(m1+m2>1){
				result = c[i][0];
				break loop;
			}
		}m1=0;m2=0;
	}}
	
	return result;
	}
	
	public String directShort(String[][] c,int x,int y,int src,int dest){
		String s=Integer.toString(src);
		String d=Integer.toString(dest);
		int m1=0;
		int m2=0;
		int n1=0;
		int n2=0;
		int best=y;
		String result="0";
		
		for(int i=0;i<x;i++){
			for(int j=1;j<y;j++){
				if(c[i][j].equals(s)){
					m1=1;
					n1=j;
				}
				if(c[i][j].equals(d)){
					m2=1;
					n2=j;
				}
				if(m1+m2>1){
					if(Math.abs(n1-n2)<best){
					result = c[i][0];
					best=Math.abs(n1-n2);
					}
				}
			}m1=0;m2=0;
		}
		
		return result;
		}
}







class Indirect {
	//some important global variables
	Set<Station> ALL=new HashSet<Station>();
	String Origin;
	int Breaker=0;
	Station Final=new Station("0");
	int best=0;
	//origin means the departure station which is the beginning station
	
	
	//the main method for getting the normal indirect route (might not be shortest)
	public String[] indirect(String[][] c,int x,int y,String Src,String des){
		//variables for passing the info about the origin station
		Origin=Src;
		int z=0;
		Station ORIGIN=new Station(Src);
		//a recursive method for getting the resulting route
		TreeDrawer(c,x,y,ORIGIN,des);
		//handling the case of not finding an indirect route
		String[] notfound=new String[1];
		notfound[0]="0";
		if(Final.getId()==0){return notfound;}
		//putting the result in an appropriate form of array of strings
		Station[] allStations=new Station[ALL.size()];
		int i=0;
		for(Station temp:ALL){
			allStations[i]=temp;
			i++;
		}
		
		int L=0;
		Station temp=Final;
		while(temp.getId()!=Integer.parseInt(Origin)){
			temp=temp.Parent;
			L++;
		}
		
		String[] Result=new String[L];
		temp=Final;
		z=L-1;
		while(temp.getId()!=Integer.parseInt(Origin)){
			Result[z]=temp.RoadToParent;
			temp=temp.Parent;
			z--;
		}
		
		return Result;
	}
	
		
	public void TreeDrawer(String[][] c,int x,int y,Station Source,String des){
		//variables for loops
		int i=0;
		int j=0;
		//a set that contains the routes that the source station exists at
		Set<Integer> SrcRoutes = new HashSet<Integer>();
		
		while(i<x){
			for(j=0;j<y;j++){
				if(c[i][j].equals(Integer.toString(Source.getId()))){SrcRoutes.add(i);}
			}i++;
		}
		//a loop to go through the other stations that shares the same route with the source station
		loop:{
		if(Breaker==1){break loop;}
		for(int Sts : SrcRoutes){
			//don't add siblings as children to avoid infinite recursion
			if(c[Sts][0].equals(Source.RoadToParent)){continue;}
			for(j=1;j<y;j++){
				//don't add the source station again and don't add a station with id zero and don't add the origin station ever
				if(!c[Sts][j].equals(Integer.toString(Source.getId()))&&!c[Sts][j].equals("0")&&!c[Sts][j].equals(Origin)){
					//if the parent is the origin and the current child is the destination don't add it to avoid getting a direct route
					if(!(Source.getId()==Integer.parseInt(Origin)&&c[Sts][j].equals(des))){
					//adding stations to the tree
					Station NS=new Station(c[Sts][j]);
					NS.Parent=Source;
					NS.RoadToParent=c[Sts][0];
					Source.Children.add(NS);
					ALL.add(NS);
					//first time to find the destination station stop the recursion and return the result
					if (NS.getId()==Integer.parseInt(des)){
						Final=NS;
						Breaker=1;
						break loop;
					}
				}
			}
		}}
		//recur through each of the children stations
		for(Station sta : Source.Children){
			TreeDrawer(c,x,y,sta,des);
		}
		}
		
	}
	
	
	
	//main method for getting the shortest indirect route
	public String[] indirectShort(String[][] c,int x,int y,String Src,String des){
		//don't recur after finding the destination
		Final.recur=false;
		Origin=Src;
		int z=0;
		Station ORIGIN=new Station(Src);
		//a recursive method for getting the shortest indirect route
		ShortestTreeDrawer(c,x,y,ORIGIN,des);
		//putting the result in an appropriate form of array of strings
		int L=0;
		Station temp=Final;
		while(temp.getId()!=Integer.parseInt(Origin)){
			temp=temp.Parent;
			L++;
		}
		
		String[] Result=new String[L];
		temp=Final;
		z=L-1;
		while(temp.getId()!=Integer.parseInt(Origin)){
			Result[z]=temp.RoadToParent;
			temp=temp.Parent;
			z--;
		}
		
		return Result;
	}
	
	
	
	
	public void ShortestTreeDrawer(String[][] c,int x,int y,Station Source,String des){
		//loops variables
		int i=0;
		int j=0;
		int k=0;
		//a list of routes the source station exists on
		List<Integer> SrcRoutes = new ArrayList<Integer>();
		
		for(i=0;i<x;i++){
			for(j=1;j<y;j++){ 
				if(c[i][j].equals(Integer.toString(Source.getId()))){SrcRoutes.add(i);}
			}
		}
		//an array containing the location of the source station on each route it exists on
		int[] SrcLocations=new int[SrcRoutes.size()];
		i=0;
		for(int Sts : SrcRoutes){
			for(j=1;j<y;j++){
				if(c[Sts][j].equals(Integer.toString(Source.getId()))){SrcLocations[i]=j;i++;}
			}
		}
		//a loop to go through all the stations that shares the same route with the source station
		i=-1;
		for(int Sts : SrcRoutes){
			i++;
			//don't add siblings as children to avoid infinite recursion
			if(c[Sts][0].equals(Source.RoadToParent)){continue;}
			for(j=1;j<y;j++){
				//a block to be broken if the destination is found and is on the currently shortest route to get to it
				For:{
				//don't add if the station id is zero or if it is the source station
				if(!c[Sts][j].equals("0")&&!c[Sts][j].equals(Integer.toString(Source.getId()))){
					//if the parent is the origin and the current child is the destination don't add it to avoid getting a direct route
					if(!(Source.getId()==Integer.parseInt(Origin)&&c[Sts][j].equals(des))){
					//a variable to hold the current total destance
					k=Source.CostToOrigin+Math.abs(SrcLocations[i]-j);
					//if the current station is the destination station and is found for the first time
					if (c[Sts][j].equals(des)&&best==0){
						Station FS=new Station(c[Sts][j]);
						FS.Parent=Source;
						FS.RoadToParent=c[Sts][0];
						FS.cost=Math.abs(SrcLocations[i]-j);
						FS.CostToOrigin=k;
						FS.recur=false;
						best=k;
						Final=FS;
						break For;
						
					}
					//if the current station is the destination station and is not found for the first time
					else if (c[Sts][j].equals(des)&&best>0&&k<best){
						Station FS=new Station(c[Sts][j]);
						FS.Parent=Source;
						FS.RoadToParent=c[Sts][0];
						FS.cost=Math.abs(SrcLocations[i]-j);
						FS.CostToOrigin=k;
						FS.recur=false;
						best=k;
						Final=FS;
						break For;
					}
					//else just add it to the tree
					else{
					Station NS=new Station(c[Sts][j]);
					NS.Parent=Source;
					NS.RoadToParent=c[Sts][0];
					Source.Children.add(NS);
					NS.cost=Math.abs(SrcLocations[i]-j);
					NS.CostToOrigin=k;
					//if the current destance is more than the best destance found stop the recursion at this branch
					if(k>best&&best>0){
					NS.recur=false;}
					}
					
				}}
			}
			}}
		//recur for each of the children
		for(Station sta : Source.Children){
			if(sta.recur){ShortestTreeDrawer(c,x,y,sta,des);}
		}
		
	}
	/*The algorithm I used here is one I made myself and I would like to call it DAVID Tree Algorithm "DTA" 
	it basically constructs a top bottom tree with the departure station as it's root
	each station forms a node and each route forms a connection between this node and it's parent node
	we begin with the root node and then add each station that is directly connected to it (except some nodes that are mentioned 
	above) each node has one parent and any number of children the method then recurs for each of the children until it is stopped 
	for a reason
	-in the normal indirect method if the destination station is found for the first time and at the same time it doesn't contribute
	a direct route the recursion stops
	-in the shortest indirect method each time we find the destination node (also with the rule of not contributing a direct route)
	we keep track of the best (least) distance found for now, the recursion stops if the distance from the current node to the 
	origin node is above the best distance or if it is one of the destination nodes
	
	the solution of the given map with the normal indirect method
	is going to be a tree like this: (departure =1,arrival =7)
	 									[1]
	 							  10   /   \
	 								 [2]    [8]
	 							20	/   \
	 							  [5]    [6]
	 						   30  |
	 							  [3]
	 						   40  |
	 							  [4]
	 			 		       50  |
	 							  [6]
	 						    /60|  \
	 						  [2] [8]  [5]
	 						   10  |
	 							 *[7]*
	 **=final destination
	 *[1],[2],...=id of the station
	10,20,...=id of the route connecting these nodes							  
	Result=[10,20,30,40,50,60,10] or [10,60,50,40,30,20,10] or any other result
	
	the solution of the given map with the shortest indirect method
	is going to be a tree like this: (departure =1,arrival =7)
	 									[1]
	 							  10,1 /   \10,2
	 								 [2]    [8] will stop recurring at some point
	 						 20,2   /   \20,3
	 							  [5]    [6]
	 						 30,3  |      |60,4
	 							  [3]    [8]
	 						 40,4  |      |10,5
	 							  [4]   *[7]* best=4
	 			 		     50,5  |
	 							  [6]
	 					 20,8 /60,6|   \20,7
	 					    [2]x  [8]  [5]x
	 						   10,7|
	 							  [7]x best=7
	 **=final destination
	 x=recursion stop for the current branch
	 [1],[2],...=id of the station
	 10,20,..=id of the route connecting these nodes
	 1,2,3,..=total distance from the departure station			  
	 Result=[10,20,60,10]
	 */
}

class Station {
	public Station(String id){
		this.id=Integer.parseInt(id);
	}
	
	public void SetId(String id){
		this.id=Integer.parseInt(id);
	}
	
	public int getId(){
		return this.id;
	}
	
	private int id;
	public Station Parent;
	public Set<Station> Children=new HashSet<Station>();
	public String RoadToParent="0";
	public int cost=0;
	public int CostToOrigin=0;
	public boolean recur=true;//if false don't recur for this station
	
}


