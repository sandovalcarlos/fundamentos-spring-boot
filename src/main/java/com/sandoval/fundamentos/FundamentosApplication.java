package com.sandoval.fundamentos;

import com.sandoval.fundamentos.bean.MyBean;
import com.sandoval.fundamentos.bean.MyBeanWithDependency;
import com.sandoval.fundamentos.bean.MyBeanWithProperties;
import com.sandoval.fundamentos.component.ComponentDependency;
import com.sandoval.fundamentos.entity.User;
import com.sandoval.fundamentos.pojo.UserPojo;
import com.sandoval.fundamentos.repository.UserRepository;
import com.sandoval.fundamentos.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean mybean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	private UserService userService;

	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository, UserService userService) {
		this.componentDependency = componentDependency;
		this.mybean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// ejemplosAnteriores();
		saveUsersInDataBase();
		getInformationJpqlFromUser();
		saveWithErrorTransactional();

	}

	private void saveWithErrorTransactional(){
		User test1 = new User("TestTransactional1", "TestTransactional1@domain.com", LocalDate.now());
		User test2 = new User("TestTransactional2", "TestTransactional2@domain.com", LocalDate.now());
		User test3 = new User("TestTransactional3", "TestTransactional1@domain.com", LocalDate.now());
		User test4 = new User("TestTransactional4", "TestTransactional4@domain.com", LocalDate.now());

		List<User> users = Arrays.asList(test1, test2, test3, test4);

		try {
			userService.saveTransactional(users);
		} catch (Exception e) {
			LOGGER.error("This a exception inside of method transactional" + e);
		}

		userService.getAllUsers()
				.stream()
				.forEach(user -> LOGGER.info("This is the user inside method transactional " + user));
	}

	private void getInformationJpqlFromUser() {
		/*LOGGER.info("User with method findByUserEmail " + userRepository.findByUserEmail("carlos@mail.com")
				.orElseThrow(()-> new RuntimeException("User not found")));
		userRepository.findAndSort("Diana", Sort.by("id").descending())
				.stream()
				.forEach(user -> LOGGER.info("User with method sort " + user));

		userRepository.findByName("Carlos")
				.forEach(user -> LOGGER.info("User with Query Method" + user));

		LOGGER.info("User with Query Method findByEmailAndName " + userRepository.findByEmailAndName("gregorio@mail.com","Gregorio Sandoval")
				.orElseThrow(()-> new RuntimeException("User not found")));

		userRepository.findByNameLike("%Ca%")
				.stream()
				.forEach(user -> LOGGER.info("User findByNameLike " + user));

		userRepository.findByEmailOrName(null, "Carlos")
				.stream()
				.forEach(user -> LOGGER.info("User findByNameOrEmail " + user));*/

		userRepository
				.findByBirthDateBetween(LocalDate.of(2021,9,1), LocalDate.of(2021,10,31))
				.stream()
				.forEach(user -> LOGGER.info("User findByBirthDateBetween " + user));

		/*userRepository.findByNameLikeOrderByIdDesc("%Carlos%")
				.stream()
				.forEach(user -> LOGGER.info("Users with like and orderby " + user));*/

		userRepository.findByNameContainingOrderByIdDesc("Carlos")
				.stream()
				.forEach(user -> LOGGER.info("Users with containing and orderby " + user));

		LOGGER.info("User as of named parameter is: " + userRepository.getAllByBirthDateAndEmail(LocalDate.of(2021,9,5), "rocio@mail.com")
				.orElseThrow(()->new RuntimeException("User not found as of named parameter")));


	}

	private void saveUsersInDataBase() {
		User user1 = new User("Carlos1", "carlos@mail.com", LocalDate.of(2021,10,22));
		User user2 = new User("Gregorio Sandoval", "gregorio@mail.com", LocalDate.of(2021,4,9));
		User user3 = new User("Diana Sandoval", "diana@mail.com", LocalDate.of(2021,9,21));
		User user4 = new User("Rocio Briones", "rocio@mail.com", LocalDate.of(2021,9,5));
		User user5 = new User("Diana Karen Sandoval", "dianakaren@mail.com", LocalDate.of(2021,9,5));
		User user6 = new User("Carlos Martin Sandoval", "carlosmartin@mail.com", LocalDate.of(2021,10,22));
		User user7 = new User("Carlos2", "carlos1@mail.com", LocalDate.of(2021,10,22));

		List<User> list = Arrays.asList(user1, user2, user3, user4, user5, user6, user7);
		list.stream().forEach(userRepository::save);

	}

	private void ejemplosAnteriores() {
		componentDependency.saludar();
		mybean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + "-" + userPojo.getPassword());
		try {
			// error
			int value = 10/0;
			LOGGER.debug("Mi valor: " + value);
		} catch (Exception e) {
			LOGGER.error("Esto es un error al dividir por cero" + e.getMessage());
		}
	}
}
