import java.util.*;
import java.text.*;
import java.io.*;

public class Main {
    //public static String password = "password";
    public static void chooseShow(ArrayList<Session> shows)
    {
        boolean showsFound=false; ArrayList<Session> foundShows=new ArrayList<Session>();
        System.out.println("Введите ваши ФИО, номер телефона, эл. почту и бюджет (каждый параметр в отдельной строке):");
        Scanner sc = new Scanner(System.in);
        String name="", phone="", email=""; int budget=0;
        try {
            name = sc.nextLine();
            phone = sc.nextLine();
            email = sc.nextLine();
            budget = sc.nextInt();
        }
        catch(Exception e)
        {
            System.out.println("Вероятно, вы ввели бюджет в неверном формате");
            return;
        }
        Client c = new Client(name, phone, email, budget);
        System.out.println("Введите параметр для выбора фильма:\n1 - время сеанса, 2 - стоимость билета, 3 - название фильма или 0 - чтобы выйти в меню");
        int parameter=sc.nextInt();
        switch(parameter)
        {
            case 1:
                System.out.println("Введите дату и время сеанса в формате дд-мм-гггг чч:мм:");
                sc.nextLine();
                String strDate = sc.nextLine();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                try {
                    Date date = dateformat.parse(strDate);
                    System.out.println(date);
                    foundShows = c.chooseShow(date, shows);
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
                if(foundShows.size()!=0)
                    showsFound=true;
                break;
            case 2:
                System.out.println("Введите стоимость билета:");
                int price = sc.nextInt();
                foundShows=c.chooseShow(price, shows);
                if(foundShows.size()!=0)
                    showsFound=true;
                break;
            case 3:
                System.out.println("Введите название фильма:");
                String movieName;
                sc.nextLine();
                movieName = sc.nextLine();
                foundShows=c.chooseShow(movieName, shows);
                if(foundShows.size()!=0)
                    showsFound=true;
                break;
            case 0:
                return;
            default:
                System.out.println("Ошибка: Вы ввели несуществующий параметр");
                return;
        }
        if(showsFound)
        {
            System.out.println("Вам подойдут следующие сеансы:");
            for(int i=0; i< foundShows.size(); i++)
            {
                System.out.println((i+1)+": "+foundShows.get(i).output());
            }
            while(true) {
                System.out.println("Выберите подходящий Вам сеанс (или введите 0, чтобы отменить выбор):");
                int ind = sc.nextInt() - 1;
                if (ind == -1)
                {
                    System.out.println("Выбор сброшен");
                    break;
                }
                {
                    if (ind >= 0 && ind < foundShows.size()) {
                        if(c.getBudget()>=foundShows.get(ind).getTheatre().getHall().getPrice())
                        {
                            Session foundShow=foundShows.get(ind);
                            System.out.println("Далее представлены свободные места на выбранном сеансе: ");
                            ArrayList<Seat> freeSeats=c.chooseSeat(foundShow.getTheatre().getHall());
                            for(int i=0; i<freeSeats.size(); i++)
                            {
                                System.out.println(freeSeats.get(i).output());
                            }
                            while(true)
                            {
                                System.out.println("Введите номера ряда и места (в одну строку):");
                                int line = sc.nextInt()-1;
                                int row = sc.nextInt()-1;
                                if(line == -1 && row ==-1)
                                    break;
                                try {
                                    if (foundShow.getTheatre().getHall().getSeats()[line][row].checkFree()) {
                                        c.setBudget(c.getBudget() - foundShow.getTheatre().getHall().getPrice());
                                        foundShow.getTheatre().getHall().getSeats()[line][row].setFree(false); //место отмечается занятым
                                        printToFile("admin/revenue info/Tickets.txt", true, c.output()+"\nСеанс: "+foundShow.output()+", "+line+ "-й ряд, "+row+"-е место\n\n");

                                        int rev = Integer.parseInt(readFromFile("admin/revenue info/TotalRevenue.txt"));
                                        rev+=foundShow.getTheatre().getHall().getPrice();
                                        printToFile("admin/revenue info/TotalRevenue.txt", false, Integer.toString(rev));

                                        int sold = Integer.parseInt(readFromFile(("admin/revenue info/TotalSold.txt")));
                                        sold++;
                                        printToFile("admin/revenue info/TotalSold.txt", false, Integer.toString(sold));

                                        int currentMovieRevenue = foundShow.getMovie().getTotalRevenue() + foundShow.getTheatre().getHall().getPrice();
                                        foundShow.getMovie().setTotalRevenue(currentMovieRevenue);
                                        System.out.println(foundShow.getMovie().getTotalRevenue() + " для теста");
                                        int currentTheatreRevenue = foundShow.getTheatre().getTotalRevenue() + foundShow.getTheatre().getHall().getPrice();
                                        foundShow.getTheatre().setTotalRevenue(currentTheatreRevenue);

                                        printToFile((String)("admin/movies/"+foundShow.getMovie().getName()+".txt"), false, Integer.toString(currentMovieRevenue));
                                        printToFile((String)("admin/theatres/"+foundShow.getTheatre().getName()+".txt"), false, Integer.toString(currentTheatreRevenue));

                                        System.out.println("Запись успешно завершена!\nСеанс: "+foundShow.output()+", "+line+ "-й ряд, "+row+"-е место");

                                        break;
                                    } else {
                                        System.out.println("Увы, это место занято. Попробуйте снова или введите \"0 0\", чтобы выйти");
                                    }
                                }
                                catch(Exception e)
                                {
                                    System.out.println("Кажется, вы ввели несуществующие места. Попробуйте снова или введите \"0 0\", чтобы выйти");
                                }
                            }
                            break;
                        }
                        else
                        {
                            System.out.println("Увы, у Вас недостаточно средств для покупки билета на данный сеанс. Попробуйте снова или выберите другой сеанс");
                        }
                    }
                    else
                    {
                        System.out.println("Введен неверный номер сеанса. Попробуйте снова");
                    }
                }
            }
        }
        else
        {
            System.out.println("Не удалось найти подходящий сеанс");
        }
    }
    public static String readFromFile(String fileName)
    {
        String strRes="";
        try(FileReader reader = new FileReader(fileName))
        {
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

    public static void main(String[] args) {
        Movie DU = new Movie("Джанго Освобожденный", 1993, "драма, военный", 195, "2D");
        printToFile((String)("admin/movies/"+DU.getName()+".txt"), false, "0");
        Movie Avatar2 = new Movie("Аватар 2 Путь воды", 2022, "фантастика, блокбастер", 192, "3D");
        printToFile((String)("admin/movies/"+Avatar2.getName()+".txt"), false, "0");
        Movie JW3 = new Movie("Джон Уик 3", 2021, "блокбастер, боевик", 39, "2D");
        printToFile((String)("admin/movies/"+JW3.getName()+".txt"), false, "0");
        CinemaTheatre Ere1 = new CinemaTheatre("Стар Ереван Плаза", "Большая Тульская, 13", new String[]{"2D", "3D"}, new CinemaHall(1, 10, 20, 300));
        CinemaTheatre FormulaCinema = new CinemaTheatre("Формула Кино", "Большая Черемушкинская, 1", new String[]{"2D", "3D"}, new CinemaHall(4, 15, 25, 500));
        CinemaTheatre GUM = new CinemaTheatre("ГУМ кинозал", "Красная площадь, 3", new String[]{"2D", "3D"}, new CinemaHall(2, 12, 15, 250));
        ArrayList<CinemaTheatre> theatres = new ArrayList<CinemaTheatre>();
        theatres.add(Ere1);
        theatres.add(FormulaCinema);
        theatres.add(GUM);
        ArrayList<Movie> movies = new ArrayList<Movie>();
        movies.add(DU);
        movies.add(Avatar2);
        movies.add(JW3);
        ArrayList<Session> shows=new ArrayList<Session>();
        shows.add(new Session(Ere1, DU, new Date(123, 0, 1, 18, 0)));
        shows.add(new Session(FormulaCinema, DU, new Date(123, 0, 2, 15, 0)));
        shows.add(new Session(Ere1, Avatar2, new Date(123, 0, 1, 12, 0)));
        shows.add(new Session(GUM, Avatar2, new Date(123, 0, 1, 18, 0)));
        shows.add(new Session(Ere1, JW3, new Date(123, 0, 2, 15, 0)));
        shows.add(new Session(FormulaCinema, JW3, new Date(123, 0, 1, 18, 0)));
        shows.add(new Session(GUM, JW3, new Date(123, 0, 2, 13, 0)));
        while (true)
        {
            System.out.println("Введите на английской раскладке client, чтобы войти как клиент или admin, чтобы войти как администратор.");
            Scanner sc = new Scanner(System.in);
            String identifier = sc.nextLine();
            if(identifier.compareTo("client")==0) {
                chooseShow(shows);
            }
            else
            {
                if(identifier.compareTo("admin")==0)
                {
                    Admin.AdminAccess(movies, theatres);
                }
                else
                    System.out.println("Неизвестный идентификатор, Попробуйте снова");
            }
            System.out.println("\n");
        }
    }
}