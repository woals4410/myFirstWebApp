package com.in28minutes.springboot.myfirstwebapp.todo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("name")
public class TodoControllerJpa {
	
	// private TodoService todoService;
	
	private TodoRepository todoRepository;
	
	
	public TodoControllerJpa(TodoRepository todoRepository) {
		super();
		this.todoRepository = todoRepository;
	}
	
	
	@RequestMapping(value="list-todos")
	public String listAllTodos(ModelMap model) {
		String username = getLoggedinUsername(); // 세션에서 name값 가져옴
		// List<Todo> todos = todoService.findByUsername(username);
		List<Todo> todos = todoRepository.findByUsername(username);
		model.addAttribute("todos", todos);
		
		return "listTodos";
	}
	
	@RequestMapping(value="add-todo", method=RequestMethod.GET)
	public String showNewTodoPage(ModelMap model) {
		String username = getLoggedinUsername();
		Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1), false);
		model.put("todo", todo);
		return "todo";
	}
	@RequestMapping(value="add-todo", method=RequestMethod.POST)
	public String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
		
		if (result.hasErrors()) {
			return "todo";
		}
		
		String username = getLoggedinUsername();
		todo.setUsername(username);
		
		// todoService.addTodo(username, todo.getDescription(),
		//					todo.getTargetDate(), todo.isDone());
		todoRepository.save(todo);
		
		return "redirect:list-todos";
	}
	
	
	@RequestMapping(value="delete-todo", method = RequestMethod.GET)
	public String deleteTodo(@RequestParam int id) {
		// todoService.deleteById(id);
		todoRepository.deleteById(id);
		
		return "redirect:list-todos";
	}
	
	@RequestMapping(value="update-todo", method = RequestMethod.GET)
	public String showUpdateTodoPage(@RequestParam int id, ModelMap model) {
		
		// Todo todo = todoService.findById(id);
		Todo todo = todoRepository.findById(id).get();
		model.addAttribute("todo", todo);
		
		return "todo";
	}
	@RequestMapping(value="update-todo", method = RequestMethod.POST)
	public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
		if (result.hasErrors()) {
			return "todo";
		}
		
		String username = getLoggedinUsername();
		todo.setUsername(username);
		
		// todoService.updateTodo(todo);
		todoRepository.save(todo);
		
		return "redirect:list-todos";
	}

	private String getLoggedinUsername() {
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
	
}
