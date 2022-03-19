package package1;

public class PoliceService {

    public static boolean checkCarEquality(Car car1, Car car2){
        if(!car1.getId().equals(car2.getId())){
            return false;
        }
        if(!car1.getProducer().equals(car2.getProducer()))
        {
            return false;
        }
        if(car1.getYear() != car2.getYear())
        {
            return false;
        }
        return true;
    }
}
