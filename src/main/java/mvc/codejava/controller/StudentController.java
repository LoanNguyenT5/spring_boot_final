package mvc.codejava.controller;

import mvc.codejava.entity.Product;
import mvc.codejava.entity.StudentEntity;
import mvc.codejava.repository.StudentRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Controller
public class StudentController {
	@Autowired
	StudentRepository studentRepository;

	@RequestMapping("/student")
	public String showNewStudentForm(Model model) {
		return "index_student";
	}
	@PostMapping(value = "/insertImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE, "text/plain;charset=UTF-8"})
	public ModelAndView save(@RequestParam("name") String name,
							 @RequestParam("age") Integer age,
							 @RequestPart("photo") MultipartFile photo) {
		try {
			StudentEntity student = new StudentEntity();
			student.setName(name);
			student.setAge(age);
			student.setPhoto(photo.getBytes());
			studentRepository.save(student);
			return new ModelAndView("redirect:/fetch");
		} catch (Exception e) {
			return new ModelAndView("student", "msg", "Error: " + e.getMessage());
		}
	}

	@GetMapping(value = "/fetch")
	public ModelAndView listStudent(ModelAndView model) throws IOException {
		List<StudentEntity> listStu = (List<StudentEntity>) studentRepository.findAll();
		model.addObject("listStu", listStu);
		model.setViewName("student");
		return model;
	}

	@GetMapping(value = "/getStudentPhoto/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> getStudentPhoto(@PathVariable("id") long id) {
		StudentEntity student = studentRepository.findById(id).orElse(null);
		if (student != null && student.getPhoto() != null) {
			return ResponseEntity.ok()
					.contentType(MediaType.IMAGE_JPEG)
					.body(student.getPhoto());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
