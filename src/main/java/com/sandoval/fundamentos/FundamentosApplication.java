package com.sandoval.fundamentos;

import com.sandoval.fundamentos.bean.MyBean;
import com.sandoval.fundamentos.bean.MyBeanWithDependency;
import com.sandoval.fundamentos.bean.MyBeanWithProperties;
import com.sandoval.fundamentos.component.ComponentDependency;
import com.sandoval.fundamentos.entity.User;
import com.sandoval.fundamentos.pojo.UserPojo;
import com.sandoval.fundamentos.repository.UserRepository;
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

	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository) {
		this.componentDependency = componentDependency;
		this.mybean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// ejemplosAnteriores();
		saveUsersInDataBase();
		getInformationJpqlFromUser();

	}

	private void getInformationJpqlFromUser() {
		LOGGER.info("User with method findByUserEmail " + userRepository.findByUserEmail("carlos@mail.com")
				.orElseThrow(()-> new RuntimeException("User not found")));
		userRepository.findAndSort("Diana", Sort.by("id").descending())
				.stream()
				.forEach(user -> LOGGER.info("User with method sort " + user));
	}

	private void saveUsersInDataBase() {
		User user1 = new User("Carlos Sandoval", "carlos@mail.com", LocalDate.of(2021,10,22));
		User user2 = new User("Gregorio Sandoval", "gregorio@mail.com", LocalDate.of(2021,4,9));
		User user3 = new User("Diana Sandoval", "diana@mail.com", LocalDate.of(2021,9,21));
		User user4 = new User("Rocio Briones", "rocio@mail.com", LocalDate.of(2021,9,5));
		User user5 = new User("Diana Karen Sandoval", "dianakaren@mail.com", LocalDate.of(2021,9,5));

		List<User> list = Arrays.asList(user1, user2, user3, user4, user5);
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
