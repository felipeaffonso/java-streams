package br.com.cvc.streams;

import br.com.cvc.streams.model.City;
import br.com.cvc.streams.model.Package;
import br.com.cvc.streams.model.SonPackage;
import br.com.cvc.streams.model.State;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class StreamsExamples {

	public static void main(String[] args) {

		final Collection<State> states = initStates();

		final Collection<Package> packages = initPackages();

		collection();

		vector();

		flatMap(states);

		map(states);

		reduce(states);

		collect(states.iterator().next());

		filter(states);

		group(packages);

		stream();

		intStream();

		streamIterate();

	}

	private static void collection() {
		final Collection<String> stringCollection = Arrays.asList("First", "Second", "Third");
		stringCollection.stream()
				.map(String::toUpperCase)
				.forEach(System.out::println);
	}

	private static void vector() {
		String[] array = {"First", "Second", "Third"};
		Arrays.stream(array)
				.map(String::toUpperCase)
				.forEach(System.out::println);
	}

	private static void stream() {
		String[] array = {"First", "Second", "Third"};
		Stream.of(array)
				.map(String::toUpperCase)
				.forEach(System.out::println);

		Stream.of("One", "Two", "Three")
				.map(String::toUpperCase)
				.forEach(System.out::println);
	}


	private static Collection<State> initStates() {

		final Collection<City> saoPauloCities = asList(
				new City(1L, "São Caetano do Sul", 80000),
				new City(2L, "Santo André", 100000),
				new City(3L, "São Bernardo do Campo", 150000)
		);

		final Collection<City> rioDeJaneiroCities = asList(
				new City(4L, "Rio de Janeiro", 200000),
				new City(5L, "São Gonçalo", 50000),
				new City(6L, "Parati", 40000)
		);

		final State saoPaulo = new State(1L, "São Paulo", saoPauloCities);
		final State rioDeJaneiro = new State(2L, "Rio de Janeiro", rioDeJaneiroCities);
		

		return asList(saoPaulo, rioDeJaneiro);
	}

	private static Collection<Package> initPackages() {
		return asList(
				new Package("Hotel Diogo", "789", 110),
				new Package("Hotel Serrano", "123", 110),
				new Package("Hotel Serrano", "123", 99),
				new Package("Hotel Diogo", "789", 100),
				new Package("Hotel Serrano", "123", 120),
				new Package("Hotel Diogo", "789", 120)
		);
	}

	private static void intStream() {
		//SingleItem or Varags
		IntStream.of(1, 3, 2, 5, 4).sorted().forEach(System.out::println);

		//Range
		IntStream.range(1, 10).forEach(System.out::println);
	}

	private static void streamIterate(){
		Stream.iterate(0, n -> n + 1)
				.limit(10)
				.forEach(System.out::println);
	}


	private static void filter(Collection<State> states) {
		System.out.println("Filtering");

		states.forEach(state -> {
			final List<City> bigCities = state.getCities().stream()
					.filter(city -> city.getPopulation() >= 100000)
					.collect(Collectors.toList());
			System.out.println(state.getName() + " has big cities: " + bigCities.stream()
					.map(City::getName).collect(Collectors.joining(", ")));
		});
	}

	private static void group(Collection<Package> packages) {
		System.out.println("Group Collecting");

		Map<String, List<Package>> packagesMap = packages.stream()
				.collect(Collectors.groupingBy(Package::getIdentifier));

		List<Package> finalList = new ArrayList<>();

		packagesMap.forEach((key, packs) -> {
			System.out.println("Key: " + key);
			System.out.println("Packages: ");
			System.out.println(packs);

			if(packs.size() == 1) {
				finalList.addAll(packs);
			} else {
				packs.sort(Comparator.comparing(Package::getPrice));
				final Package principal = packs.get(0);
				final List<SonPackage> sons = packs.stream()
						.map(p -> new SonPackage(p.getHotel(), p.getFlight(), p.getPrice()))
						.collect(Collectors.toList());
				principal.setSonPackages(sons);
				finalList.add(principal);
			}
		});

		finalList.sort(Comparator.comparing(Package::getPrice));

		System.out.println("Final List: ");
		System.out.println(finalList);

	}

	private static void collect(State state) {
		System.out.println("Collecting (mutable reduction)");

		System.out.println("Using BiConsumers");
		final List<String> citiesBiConsumers = state.getCities()
				.stream()
				.collect(() -> new ArrayList<>(),
						(c, e) ->  c.add(e.getName()),
						(c1, c2) -> c1.addAll(c2));

		System.out.println(citiesBiConsumers);

		System.out.println("Combining with map");
		final List<String> citiesMap = state.getCities()
				.stream()
				.map(City::getName)
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

		System.out.println(citiesMap);

		System.out.println("Using Collectors");
		final List<String> citiesCollectors = state.getCities()
				.stream()
				.map(City::getName)
				.collect(Collectors.toList());

		System.out.println(citiesCollectors);

		System.out.println("Creating Collectors");
		final Collector<City, ?, Integer> summingPopulations = Collectors.summingInt(City::getPopulation);

		final Integer citiesPopulation = state.getCities()
				.stream()
				.collect(summingPopulations);

		System.out.println("Cities Total Population: " + citiesPopulation);

	}

	private static void reduce(Collection<State> states) {
		System.out.println("Reducing States Population");

		states.stream()
				.map(state -> state.getName() + " - Population: " +
						state.getCities()
								.stream()
								.mapToInt(City::getPopulation)
								.reduce(0, Integer::sum)
				)
				.forEach(System.out::println);

		System.out.println("Max Population by State");

		states.stream()
				.map(state -> state.getName() + " - Higher Population: " +
						state.getCities()
								.stream()
								.mapToInt(City::getPopulation)
								.max()
								.getAsInt()
				)
				.forEach(System.out::println);
	}

	private static void flatMap(Collection<State> states) {
		System.out.println(states);

		final List<City> cities = states.stream()
				.flatMap(s -> s.getCities().stream())
				.sorted(Comparator.comparing(City::getName))
				.collect(Collectors.toList());

		System.out.println(cities);
	}

	private static void map(Collection<State> states) {
		System.out.println(states);

		states.stream()
				.map(state -> state.getName() + ": " +
						state.getCities()
								.stream()
								.sorted(Comparator.comparing(City::getName))
								.map(City::getName)
								.collect(Collectors.joining(", "))
				)
				.forEach(System.out::println);

	}

}