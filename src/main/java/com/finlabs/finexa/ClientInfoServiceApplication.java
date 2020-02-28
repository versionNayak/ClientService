package com.finlabs.finexa;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.finlabs.finexa.model.CacheInfoDTO;
import com.finlabs.finexa.model.MasterFinexaException;
import com.finlabs.finexa.model.UserClientRedis;
import com.finlabs.finexa.model.UserInRedis;
import com.finlabs.finexa.resources.service.AdvanceLoanCalculatorService;
import com.finlabs.finexa.resources.service.Annuity2ProductService;
import com.finlabs.finexa.resources.service.AtalPensionYojanaService;
import com.finlabs.finexa.resources.service.BankFDSTDRCDCPService;
import com.finlabs.finexa.resources.service.BankFDTDRService;
import com.finlabs.finexa.resources.service.BankRecurringDespositService;
import com.finlabs.finexa.resources.service.BondDebenturesService;
import com.finlabs.finexa.resources.service.EPF2Service;
import com.finlabs.finexa.resources.service.EquityCalculatorService;
import com.finlabs.finexa.resources.service.KisanVikasPatraService;
import com.finlabs.finexa.resources.service.MutualFundLumpsumSipService;
import com.finlabs.finexa.resources.service.NPSCalService;
import com.finlabs.finexa.resources.service.PONSCService;
import com.finlabs.finexa.resources.service.PORecurringDespositService;
import com.finlabs.finexa.resources.service.POTimeDespositService;
import com.finlabs.finexa.resources.service.PPFFixedAmountService;
import com.finlabs.finexa.resources.service.PerpetualBondService;
import com.finlabs.finexa.resources.service.PostOfficeMonthlyIncomeSchemeService;
import com.finlabs.finexa.resources.service.SeniorCitizenSavingSchemeService;
import com.finlabs.finexa.resources.service.SimpleLoanCalEMIBasedService;
import com.finlabs.finexa.resources.service.SimpleLoanCalNonEMIBasedService;
import com.finlabs.finexa.resources.service.SukanyaSamriddhiSchemeService;
import com.finlabs.finexa.resources.service.ZeroCouponService;
import com.finlabs.finexa.service.AdvisorService;

@SpringBootApplication
@EnableAutoConfiguration
public class ClientInfoServiceApplication extends SpringBootServletInitializer {

	private EntityManager entityManager;

	@Autowired
	private AdvisorService advisorService;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ClientInfoServiceApplication.class, args);
	}

	/*
	 * @Bean
	 * 
	 * @Order(value = 0) public FilterRegistrationBean
	 * sessionRepositoryFilterRegistration(SessionRepositoryFilter
	 * springSessionRepositoryFilter) {
	 * System.out.println("springSessionRepositoryFilter"); FilterRegistrationBean
	 * filterRegistrationBean = new FilterRegistrationBean();
	 * filterRegistrationBean.setFilter(new
	 * DelegatingFilterProxy(springSessionRepositoryFilter));
	 * filterRegistrationBean.setUrlPatterns(Arrays.asList("/*")); return
	 * filterRegistrationBean; }
	 */

	/*
	 * @Bean public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
	 * return new SecuirityConfig(); }
	 */

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Calcutta"));

	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ClientInfoServiceApplication.class);
	}

	@Bean
	public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(false);

		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.show_sql", "false");
		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
		localContainerEntityManagerFactoryBean.setPackagesToScan("com.finlabs.finexa");
		localContainerEntityManagerFactoryBean.setDataSource(dataSource);
		localContainerEntityManagerFactoryBean.setJpaProperties(jpaProperties);
		localContainerEntityManagerFactoryBean.afterPropertiesSet();
		this.entityManager = localContainerEntityManagerFactoryBean.getObject().createEntityManager();
		return localContainerEntityManagerFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
		return jpaTransactionManager;
	}

	@Bean
	public Mapper mapper() {
		return new DozerBeanMapper();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				System.out.println("cors configure");
				registry.addMapping("/**").allowedOrigins("http://localhost", "http://192.168.1.110",
						"https://192.168.1.110", "http://testing.finexa.in", "https://testing.finexa.in",
						"https://staging.finexa.in", "https://app.finexa.in", "http://app.finexa.in",
						"https://10.52.218.238", "http://10.52.218.238");
			}
		};
	}

	@Bean
	@DependsOn("entityManagerFactory")
	public SessionFactory sessionfactory() {
		Session session = entityManager.unwrap(Session.class);
		SessionFactory sessionFactory = session.getSessionFactory();
		return sessionFactory;
	}

	@Bean
	public AdvanceLoanCalculatorService advanceLoanCalculatorService() {
		return new AdvanceLoanCalculatorService();
	}

	@Bean
	public Annuity2ProductService annuity2ProductService() {
		return new Annuity2ProductService();
	}

	@Bean
	public AtalPensionYojanaService atalPensionYojanaService() {
		return new AtalPensionYojanaService();
	}

	@Bean
	public BankFDSTDRCDCPService bankFDSTDRCDCPService() {
		return new BankFDSTDRCDCPService();
	}

	@Bean
	public BankFDTDRService bankFDTDRService() {
		return new BankFDTDRService();
	}

	@Bean
	public BankRecurringDespositService bankRecurringDespositService() {
		return new BankRecurringDespositService();
	}

	@Bean
	public BondDebenturesService bondDebenturesService() {
		return new BondDebenturesService();
	}

	@Bean
	public EPF2Service ePF2Service() {
		return new EPF2Service();
	}

	@Bean
	public EquityCalculatorService equityCalculatorService() {
		return new EquityCalculatorService();
	}

	@Bean
	public KisanVikasPatraService kisanVikasPatraService() {
		return new KisanVikasPatraService();
	}

	@Bean
	public MutualFundLumpsumSipService mutualFundLumpsumSipService() {
		return new MutualFundLumpsumSipService();
	}

	@Bean
	public NPSCalService nPSCalService() {
		return new NPSCalService();
	}

	@Bean
	public PerpetualBondService perpetualBondService() {
		return new PerpetualBondService();
	}

	@Bean
	public PONSCService pONSCService() {
		return new PONSCService();
	}

	@Bean
	public PORecurringDespositService pORecurringDespositService() {
		return new PORecurringDespositService();
	}

	@Bean
	public PostOfficeMonthlyIncomeSchemeService postOfficeMonthlyIncomeSchemeService() {
		return new PostOfficeMonthlyIncomeSchemeService();
	}

	@Bean
	public POTimeDespositService pOTimeDespositService() {
		return new POTimeDespositService();
	}

	@Bean
	public PPFFixedAmountService pPFFixedAmountService() {
		return new PPFFixedAmountService();
	}

	@Bean
	public SeniorCitizenSavingSchemeService seniorCitizenSavingSchemeService() {
		return new SeniorCitizenSavingSchemeService();
	}

	@Bean
	public SimpleLoanCalEMIBasedService simpleLoanCalEMIBasedService() {
		return new SimpleLoanCalEMIBasedService();
	}

	@Bean
	public SimpleLoanCalNonEMIBasedService simpleLoanCalNonEMIBasedService() {
		return new SimpleLoanCalNonEMIBasedService();
	}

	@Bean
	public SukanyaSamriddhiSchemeService sukanyaSamriddhiSchemeService() {
		return new SukanyaSamriddhiSchemeService();
	}

	@Bean
	public ZeroCouponService zeroCouponService() {
		return new ZeroCouponService();
	}

	@Bean
	public Map<String, String> exceptionmap() {
		@SuppressWarnings("unchecked")
		List<MasterFinexaException> listMasterFinexaException = entityManager
				.createQuery("SELECT mFinexaExp FROM MasterFinexaException mFinexaExp").getResultList();
		// masterFinexaExceptionRepository.findAll();
		Map<String, String> exceptionMap = listMasterFinexaException.stream()
				.collect(Collectors.toMap(mfe -> mfe.getErrorCode(), mfe -> mfe.getErrorDescription()));
		return exceptionMap;

	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		System.out.println("jedisConnectionFactory");
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName("localhost");
		factory.setPort(6379);
		factory.setUsePool(true);
		return factory;
	}

	@Bean
	RedisTemplate<String, CacheInfoDTO> redisTemplate2() {
		System.out.println("redisTemplate");
		RedisTemplate<String, CacheInfoDTO> redisTemplate = new RedisTemplate<String, CacheInfoDTO>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	@Bean
	public RedisTemplate<String, UserInRedis> redisTemplate1() {

		RedisTemplate<String, UserInRedis> redisTemplate = new RedisTemplate<String, UserInRedis>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	@Bean
	RedisTemplate<String, UserClientRedis> redisTemplate() {
		System.out.println("redisTemplate");
		RedisTemplate<String, UserClientRedis> redisTemplate = new RedisTemplate<String, UserClientRedis>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	@Bean
	RedisMessageListenerContainer keyExpirationListenerContainer() {
		System.out.println("RedisMessageListenerContainer");
		RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
		listenerContainer.setConnectionFactory(jedisConnectionFactory());
		listenerContainer.addMessageListener(messageListener(), new PatternTopic("__keyevent@*__:expired"));
		listenerContainer
				.setErrorHandler(e -> logger.error("There was an error in redis key expiration listener container", e));
		return listenerContainer;
	}

	@Bean
	MessageListenerAdapter messageListener() {
		System.out.println("MessageListenerAdapter");
		return new MessageListenerAdapter(new ExpirationListner(advisorService));
	}
	/*
	 * @Bean CacheManager cacheManager() { return new
	 * RedisCacheManager(redisTemplate()); }
	 */

	/*
	 * @Bean public CacheInfoService cacheInfoService() {
	 * 
	 * return new CacheInfoService(); }
	 */

	/*
	 * @Bean public TomcatEmbeddedServletContainerFactory tomcatEmbedded() {
	 * 
	 * TomcatEmbeddedServletContainerFactory tomcat = new
	 * TomcatEmbeddedServletContainerFactory();
	 * 
	 * tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> { if
	 * ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) { //
	 * -1 means unlimited ((AbstractHttp11Protocol<?>)
	 * connector.getProtocolHandler()).setMaxSwallowSize(-1); } });
	 * 
	 * return tomcat;
	 * 
	 * }
	 */

}
