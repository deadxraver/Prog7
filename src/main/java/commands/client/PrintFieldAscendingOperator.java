package commands.client;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.Person;
import elements.User;

import java.lang.management.OperatingSystemMXBean;
import java.util.Arrays;

public class PrintFieldAscendingOperator implements ClientCommand {
	private static MovieCollection movieCollection;
	public PrintFieldAscendingOperator(MovieCollection movieCollection) {
		PrintFieldAscendingOperator.movieCollection = movieCollection;
	}
	@Override
	public ResponsePackage run(String username, String password, Object args) {
		Object[] operatorList = movieCollection.getOperatorList();
		Arrays.sort(operatorList);
		StringBuilder stringBuilder = new StringBuilder();
		for (Object o : operatorList) {
			stringBuilder.append(o).append('\n');
		}
		return new ResponsePackage(
				false,
				stringBuilder.toString(),
				null
		);
	}

	@Override
	public String explain() {
		return """
				<==>
				print_field_ascending_operator - prints 
				<==>
				""";
	}
}
