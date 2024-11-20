package mvc.codejava.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mvc.codejava.entity.Role;
import mvc.codejava.repository.ProductRepository;
import mvc.codejava.repository.RoleRepository;
import mvc.codejava.service.ProductService;
import mvc.codejava.repository.UserRepository;
import mvc.codejava.entity.Product;
import mvc.codejava.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {
	@Autowired
	private ProductService service;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private RoleRepository roleRepository;

	@RequestMapping("/")
	public String viewHomePage(Model model) {
		List<User> userList = (List<User>) userRepository.findAll();
		if(userList.isEmpty()){
			Role role = new Role();
			role.setName("ADMIN");
			roleRepository.save(role);

			User user = new User();
			user.setEnabled(true);
			user.setUsername("admin");

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode("123456");
			user.setPassword(encodedPassword);
			Set<Role> roles = new HashSet<>();
			roles.add(role);
			user.setRoles(roles);
			userRepository.save(user);
		}

		if(!productRepository.existsById(1L)){
			for(int i = 1; i <10; i++){
				Product p = new Product();
				p.setBrand("Apple " + i);
				p.setMadein("China " + i);
				p.setPrice(10 + i);
				p.setName("iPhone "+ 5 + i);
				productRepository.save(p);
			}
		}
		List<Product> listProducts = service.listAll();
		model.addAttribute("listProducts", listProducts);
		
		return "index";
	}
	
	@RequestMapping("/new")
	public String showNewProductForm(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		
		return "new_product";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("product") Product product) {
		service.save(product);
		
		return "redirect:/";
	}
	
	@RequestMapping("/edit/{id}")
	public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("edit_product");
		
		Product product = service.get(id);
		mav.addObject("product", product);
		
		return mav;
	}	
	
	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Long id) {
		service.delete(id);
		
		return "redirect:/";
	}
}
