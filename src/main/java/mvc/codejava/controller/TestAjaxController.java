package mvc.codejava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestAjaxController {
    @RequestMapping("/add")
    public String viewPasyment(Model model) {
        return "payment/add";
    }
    @PostMapping("/add-data")
    @ResponseBody
    public String addData(@RequestBody User user) {
        // Xử lý dữ liệu gửi đến từ frontend
        String jsonResponse = String.format("{\"name\": \"%s\", \"age\": %d}", user.getName(), user.getAge());

        // Trả về chuỗi JSON
        return jsonResponse;
    }

    // Class User để ánh xạ dữ liệu JSON gửi lên từ frontend
    public static class User {
        private String name;
        private int age;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
