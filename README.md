# LetsCharge-Application
Letscharge Springboot Application

##--------------------------------

 # Entity  :
	Customer :
	    private String customer_id;
	    private String first_name;
	    private String last_name;
	    private String email;
	    private StatusEnum status;

	Plan:
	    private String plan_id;
	    private String plan_name;
	    private PlanTypeEnum planType;
	    private Integer plan_frequency;
	    private StatusEnum status;
	    private Date created_at;

	Product:
	 
	    private String product_id;
	    private String product_name;
	    private String product_code;
	    private Double productPrice;
	    private StatusEnum status;
	    private Date created_at;
	    
	Subscription:
	    
	    private String subscription_id;
	    private Date activated_at;
	    private Integer quantity;
	    private StatusEnum status;
	    private String first_name;
	    private String last_name;
	    private String email;
	    private String product_name;
	    private double product_price;
	    private String plan_name;
	    private com.example.LetsCharge.services.model.Plan.PlanTypeEnum plan_type;
	    private int plan_frequency;
	    public Customer customer;
	    public Plan plan;
	    public Product product;
	    
	Order:
	
	    private String order_id;
	    private String subscription_id;
	    private String order_generation_date;
	    private Plan.PlanTypeEnum planType;
	    private double order_total;
	    private String next_order_date;
	    public Order_details order_details ;


##--------------------------------------------

added updateSubscriptionPatch api to update subscription status(running,cancel)
