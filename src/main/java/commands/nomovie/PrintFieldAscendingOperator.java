package commands.nomovie;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.Person;
import elements.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class PrintFieldAscendingOperator implements NoMovieCommand, Serializable {

    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object object) {
        StringBuilder builder = new StringBuilder();
        Object[] objects = movieCollection.getOperatorList();
        Person[] people = (Person[]) objects;
        Arrays.sort(people);
        for (Person person : people) {
            builder.append(person);
        }
        return new ResponsePackage(
                false,
                builder.toString(),
                null
        );
    }

    @Override
    public String explain() {
        return """
                <==>
                print_field_ascending_operator : prints collection sorted by operator ascending
                print_field_ascending_operator
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = 3073911489917437176L;
}
