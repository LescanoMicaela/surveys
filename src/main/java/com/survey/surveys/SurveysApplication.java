package com.survey.surveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@SpringBootApplication
public class SurveysApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveysApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UserRepository userRepo, QuestionRepository questionRepo, SurveyRepository surveyRepo,
									  SurveyQuestionRepository surveyQuestionrepo, UserSurveyRepository userSurveyRepo,
									  UserSurveyAnswerRepository userSurveyAnswerRepo){
		return args -> {
			// save a couple of customers
			User alasdair = new User("Alasdar","hola123","alasdair@gmail.com");
			User mica = new User("Alasdar","hola123","alasdair@gmail.com");
			User adonis = new User("Alasdar","hola123","alasdair@gmail.com");
			userRepo.save(alasdair);
			userRepo.save(mica);
			userRepo.save(adonis);

			Question q1 = new Question("Overall, how would you rate the quality of your customer service experience?");
			Question q2 = new Question("How well did we understand your questions and concerns?");
			Question q3 = new Question("How likely is it that you would recommend this company to a friend or colleague?");
			Question q4 = new Question("How much time did it take us to address your questions and concerns?");
			Question q5 = new Question("Do you have any other comments, questions, or concerns?");
			Question q6 = new Question("How would you rate the value for money of the product?");
			Question q7 = new Question("How responsive have we been to your questions or concerns about our products?");
			Question q8 = new Question("How long have you been a customer of our company?");
			Question q9 = new Question("How likely are you to purchase any of our products again?");
			Question q10 = new Question("Do you have any other comments, questions, or concerns?");

			questionRepo.save(q1);
			questionRepo.save(q2);
			questionRepo.save(q3);
			questionRepo.save(q4);
			questionRepo.save(q5);
			questionRepo.save(q6);
			questionRepo.save(q7);
			questionRepo.save(q8);
			questionRepo.save(q9);
			questionRepo.save(q10);

			Survey survey1 = new Survey("Customer Satisfaction");
			surveyRepo.save(survey1);


			surveyQuestionrepo.save(new SurveyQuestion(survey1,q1));
			surveyQuestionrepo.save(new SurveyQuestion(survey1,q10));
			surveyQuestionrepo.save(new SurveyQuestion(survey1,q2));
			surveyQuestionrepo.save(new SurveyQuestion(survey1,q3));
			surveyQuestionrepo.save(new SurveyQuestion(survey1,q4));
			surveyQuestionrepo.save(new SurveyQuestion(survey1,q5));
			surveyQuestionrepo.save(new SurveyQuestion(survey1,q6));
			surveyQuestionrepo.save(new SurveyQuestion(survey1,q7));
			surveyQuestionrepo.save(new SurveyQuestion(survey1,q8));
			surveyQuestionrepo.save(new SurveyQuestion(survey1,q9));

			UserSurvey us1 = new UserSurvey(alasdair,survey1);
			UserSurvey us2 = new UserSurvey(mica,survey1);
			UserSurvey us3 = new UserSurvey(adonis,survey1);

			userSurveyRepo.save(us1);
			userSurveyRepo.save(us2);
			userSurveyRepo.save(us3);

			UserSurveyAnswer usa1 = new UserSurveyAnswer(us1,q1,"Great");
			UserSurveyAnswer usa2 = new UserSurveyAnswer(us1,q1,"Very well");
			UserSurveyAnswer usa3 = new UserSurveyAnswer(us1,q1,"Very likely");
			UserSurveyAnswer usa4 = new UserSurveyAnswer(us1,q1,"3 days");
			UserSurveyAnswer usa5 = new UserSurveyAnswer(us1,q1,"No");
			UserSurveyAnswer usa6 = new UserSurveyAnswer(us1,q1,"Fair well");
			UserSurveyAnswer usa7 = new UserSurveyAnswer(us1,q1,"Very well");
			UserSurveyAnswer usa8 = new UserSurveyAnswer(us1,q1,"2 years");
			UserSurveyAnswer usa9 = new UserSurveyAnswer(us1,q1,"Very likely");
			UserSurveyAnswer usa10 = new UserSurveyAnswer(us1,q1,"No");

			userSurveyAnswerRepo.save(usa1);
			userSurveyAnswerRepo.save(usa2);
			userSurveyAnswerRepo.save(usa3);
			userSurveyAnswerRepo.save(usa4);
			userSurveyAnswerRepo.save(usa5);
			userSurveyAnswerRepo.save(usa6);
			userSurveyAnswerRepo.save(usa7);
			userSurveyAnswerRepo.save(usa8);
			userSurveyAnswerRepo.save(usa9);
			userSurveyAnswerRepo.save(usa10);



		};
	}
}


@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	UserRepository userRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			User user = userRepository.findByUserName(inputName);
			if (user != null) {
				return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()

				.antMatchers("**").permitAll()
//				.antMatchers("/api/games**").permitAll()
//				.antMatchers("/api/players**").permitAll()
//				.antMatchers("/web/script/**").permitAll()
//				.antMatchers("/web/styles/**").permitAll()
////				.antMatchers("/api/games").permitAll()
//				.antMatchers("/**").hasAnyAuthority("USER")

				.and()
				.formLogin()
				.usernameParameter("email")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}

}


