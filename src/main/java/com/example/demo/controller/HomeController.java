package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.UserRepository;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;

@Controller
public class HomeController {
@Autowired
	private UserRepository userRepository;
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("titel", "HOME-Smart-Contact-Manager");
		return "home";
		
	}
	
	
	@GetMapping("/all_photo")
	public String allPhotos(Model model) {
		model.addAttribute("titel", "Add-Pdotos-Smart-Contact-Manager");
		
		List<User> users=userRepository.findAll();
		model.addAttribute("users",users );
		
		return "all_photo";
		
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("titel", "SignUp-Smart-Contact-Manager");
		model.addAttribute("user",new User());
		return "signup";
		
	}
	@PostMapping("/do_register")
	public String doRegister(@ModelAttribute("user") User user,@RequestParam("image") MultipartFile file, Model model,HttpSession session) throws IOException {
		
		
			user.setRole("admin");
			user.setImageUrl(file.getOriginalFilename());
		
			System.out.println(user);
			
			File saveFile=new ClassPathResource("static/img").getFile();
			Path path=Paths.get(saveFile.getAbsoluteFile()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			
			User result=this.userRepository.save(user);
			
			
			
			return "home";
		
		
		
		
	}
	
	@GetMapping("/delete/{id}")
	public String deletPhoto(@PathVariable("id") Integer id) throws IOException {
		
		//Optional<User> u=userRepository.findById(id);
		
		Optional<User> u=userRepository.findById(id);
		User u1=u.get();
		userRepository.deleteById(id);
		File deleteFile=new ClassPathResource("static/img").getFile();
	Path path=Paths.get(deleteFile.getAbsoluteFile()+File.separator+u1.getImageUrl());
 	Files.delete(path);
		return "home";
		
	}
	
	


}
