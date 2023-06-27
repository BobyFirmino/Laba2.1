import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.text.*;
import java.io.*;
import java.nio.file.Paths;
public class Admin {
    private String username;
    private static String password = "qwerty";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public static String readFromFile(String fileName)
    {
        String strRes="";
        try(FileReader reader = new FileReader(fileName))
        {
            // читаем посимвольно
            int a;
            while((a=reader.read())!=-1){

                strRes+=(char)a;
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        return strRes;
    }
    public static void printToFile(String fileName, boolean append, String text)//append: true = дописать новые данные в конец файла, false = перезаписать содержимое файла
    {
        try(FileWriter writer = new FileWriter(fileName, append))
        {
            writer.write(text);
            writer.flush();
            writer.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    public static void TotalRevenue()
    {
        System.out.println("Общая выручка составила " + readFromFile("admin/revenue info/TotalRevenue.txt") + "руб");//допилить
    }
    public static void TotalTickets()
    {
        System.out.println("Общее число купленных билетов составило " + readFromFile("admin/revenue info/TotalSold.txt") + " билетов");
    }
    public static void AllTickets()
    {
        String allTickets=readFromFile("admin/revenue info/Tickets.txt");
        if(allTickets.compareTo((""))==0)
            System.out.println("К сожалению, список пуст");
        else
            System.out.print("Далее представлен список всех купленных билетов:\n"+allTickets);
    }
    public static void MovieRevenue(ArrayList<Movie> movies)
    {
        while (true) {
            System.out.println("Выберите один из следующих фильмов или введите 0, чтобы выйти:");
            for(int i=0; i<movies.size(); i++)
                System.out.println((Integer)(i+1)+" - " + movies.get(i).getName());
            Scanner sc = new Scanner(System.in);
            int movieIndex = sc.nextInt();
            if (movieIndex > 0 && movieIndex <= movies.size()) {
                System.out.println("Выручка по фильму " + movies.get(movieIndex-1).getName() + " составила " + movies.get(movieIndex-1).getTotalRevenue() + " руб");
                return;
            } else {
                if (movieIndex == -1)
                    return;
                else
                    System.out.println("Введен неправильный индекс, попробуйте снова");
            }
        }
    }
    public static void NotRentalMovie(ArrayList<Movie> movies)
    {
        int minRevenue = movies.get(0).getTotalRevenue();
        ArrayList<Movie> notRental = new ArrayList<>();
        for(int i=0; i<movies.size(); i++)
        {
            if(movies.get(i).getTotalRevenue()<=minRevenue)
            {
                minRevenue = movies.get(i).getTotalRevenue();
            }
        }
        for(int i=0; i<movies.size(); i++)
        {
            if (movies.get(i).getTotalRevenue()==minRevenue)
                notRental.add(movies.get(i));
        }
        System.out.println("Список нерентабельных фильмов: ");
        for(int i=0; i<notRental.size(); i++)
            System.out.println(notRental.get(i).getName() + " - " +notRental.get(i).getTotalRevenue() +" руб");

    }
    public static void NotRentalTheatre(ArrayList<CinemaTheatre> theatres)
    {
        int minThRevenue = theatres.get(0).getTotalRevenue();
        ArrayList<CinemaTheatre> notRentalTh = new ArrayList<>();
        for(int i=0; i<theatres.size(); i++)
        {
            if(theatres.get(i).getTotalRevenue()<=minThRevenue)
            {
                minThRevenue = theatres.get(i).getTotalRevenue();
            }
        }
        for(int i=0; i<theatres.size(); i++)
        {
            if (theatres.get(i).getTotalRevenue()==minThRevenue)
                notRentalTh.add(theatres.get(i));
        }
        System.out.println("Список нерентабельных кинотеатров: ");
        for(int i=0; i<notRentalTh.size(); i++)
            System.out.println(notRentalTh.get(i).getName() + " - " +notRentalTh.get(i).getTotalRevenue() +" руб");

    }
    public static void AdminAccess(ArrayList<Movie> movies, ArrayList<CinemaTheatre> theatres)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите пароль:");
        String pass = sc.nextLine();
        if (pass.compareTo(password)==0) {
            System.out.println("Вы в режиме администратора");
            while(true)
            {

                System.out.println("1 - общая выручка\n2 - общее число проданных билетов\n3 - все проданные билеты\n4 - выручка по фильму\n5 - нерентабельные фильмы\n6 - нерентабельные кинотеатры\n0 - вернуться в изначальное меню");
                int a=sc.nextInt();
                switch (a) {
                    case 1:
                        Admin.TotalRevenue();
                        break;
                    case 2:
                        Admin.TotalTickets();
                        break;
                    case 3:
                        Admin.AllTickets();
                        break;
                    case 4:
                        Admin.MovieRevenue(movies);
                        break;
                    case 5:
                        Admin.NotRentalMovie(movies);
                        break;
                    case 6:
                        Admin.NotRentalTheatre(theatres);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Введено неверное значение");
                }
            }
        }
        else {
            System.out.println("Неверный пароль");
        }
    }
}
