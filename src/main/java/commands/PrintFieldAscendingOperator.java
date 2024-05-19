package commands;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.Person;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class PrintFieldAscendingOperator implements Command, Serializable {

    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object object) {
        StringBuilder builder = new StringBuilder();
        Object[] objects = movieCollection.getOperatorList();
        Person[] people = (Person[]) objects;
        Arrays.sort(people);
        for (Person person : people) {
            builder.append(person);
        }
        return new ResponsePackage(
                false,
                builder.toString()
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
