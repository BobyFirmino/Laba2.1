import java.util.ArrayList;
import java.util.Date;

public class Client {
    private String name;
    private String phoneNumber;
    private String email;
    private int budget;
    public Client()
    {
        name="Иванов Иван Иванович";
        phoneNumber="88005553535";
        email="iviviv@iv.iv";
        budget=1000;
    }
    public Client(String name, String phoneNumber, String email, int budget)
    {
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.email=email;
        this.budget=budget;
    }
    public String getName()
    {
        return name;
    }
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    public String getEmail()
    {
        return email;
    }
    public int getBudget()
    {
        return budget;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber=phoneNumber;
    }
    public void setEmail(String email)
    {
        this.email=email;
    }
    public void setBudget(int budget)
    {
        this.budget=budget;
    }
     public ArrayList<Session> chooseShow(Date wantedTime, ArrayList<Session> shows)
     {
         ArrayList<Session> result=new ArrayList<Session>();
        for(int i=0; i< shows.size(); i++)
        {
            if(wantedTime.compareTo(shows.get(i).getTime())==0)
                result.add(shows.get(i));
        }
        return result;
     }
    public ArrayList<Session> chooseShow(int wantedPrice, ArrayList<Session> shows)
    {
        ArrayList<Session> result=new ArrayList<Session>();
        for(int i=0; i< shows.size(); i++)
        {
            if(shows.get(i).getTheatre().getHall().getPrice()==wantedPrice)
                result.add(shows.get(i));
        }
        return result;
    }
    public ArrayList<Session> chooseShow(String wantedName, ArrayList<Session> shows)
    {
        ArrayList<Session> result=new ArrayList<Session>();
        for(int i=0; i< shows.size(); i++)
        {
            if(wantedName.compareTo(shows.get(i).getMovie().getName())==0)
                result.add(shows.get(i));
        }
        return result;
    }
    public ArrayList<Seat> chooseSeat(CinemaHall hall)
    {
        return hall.returnFreeSeats();
    }
    public String output()
    {
        return new String("Клиент: "+ name +" "+phoneNumber+" "+ email +"; остаток средств "+ budget);
    }
}
