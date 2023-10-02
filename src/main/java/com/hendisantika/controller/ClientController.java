package com.hendisantika.controller;

import com.hendisantika.model.Client;
import com.hendisantika.service.impl.ClientServiceImpl;
import com.hendisantika.service.impl.UploadFileServiceImpl;
import com.hendisantika.util.PageRender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-invoice-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 15/09/21
 * Time: 05.40
 */
@Controller
@SessionAttributes("client")
public class ClientController {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final String ERROR = "error";
    private static final String RDRECT_CLIENTS = "redirect:/clients";
    private static final String CLIENT = "client";
    private static final String FORM = "/form";
    private static final String TITLE = "title";


    //@Qualifier ("clientDao") If we have several implementations
    //of the interface, we indicate which one we want to use by giving its name
    //If we only have one, we use @Autowired
    private final ClientServiceImpl clientServiceImpl;

    private final UploadFileServiceImpl uploadFileServiceImpl;

    private final MessageSource messageSource;

    public ClientController(ClientServiceImpl clientServiceImpl, UploadFileServiceImpl uploadFileServiceImpl, MessageSource messageSource) {
        this.clientServiceImpl = clientServiceImpl;
        this.uploadFileServiceImpl = uploadFileServiceImpl;
        this.messageSource = messageSource;
    }

    //Method for uploading images
    //We write the parameter {filename:. +} As a regular expression
    //since if not Spring would remove the extension of it (from the file)
    //and we need it to find the file on the computer.
    //When the view tries to upload the image from the uploads folder
    //this method is called
    @Secured("ROLE_USER")
    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> viewFoto(@PathVariable String filename) {
        Resource resource;
        resource = uploadFileServiceImpl.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    //@Secured("ROLE_USER")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/view/{id}")
    public String ver(@PathVariable Long id, Map<String, Object> model, RedirectAttributes flash) {
        //Client client = clientService.findOne(id);
        Client client = clientServiceImpl.fetchByIdWithInvoice(id);
        if (client == null) {
            flash.addFlashAttribute(ERROR, "The client does not exist in the database");
            return RDRECT_CLIENTS;
        } else {
            model.put(CLIENT, client);
            model.put(TITLE, "Customer details - " + client.getName());
        }
        return "view";
    }

    @GetMapping(value = {"/clients", "/"})
    public String lists(@RequestParam(defaultValue = "0") int page,
                        Model model,
                        Authentication authentication,
                        HttpServletRequest request,
                        Locale locale) {
        if (authentication != null) {
            log.info("The current user is {}", authentication.getName());
        }
        /*
         * We can also get access to authentication without injecting it, through the
         * static method SecurityContextHolder.getContext (). getAuthentication ();
         * This allows us to use it in any class
         */

        //We check if the user has the necessary role for this resource
        if (hasRole()) {
            log.info("The user has the necessary role to access this resource");
        } else {
            log.error("The user does NOT have the necessary role to access this resource");
        }
        //In this way we can do the same, without having to implement the hasRole () method
		/*SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper
		(request, "ROLE_");
		if(securityContext.isUserInRole("ADMIN")) {
			log.info("Using SecurityContextHolderAwareRequestWrapper: The user has the necessary role to access
to this resource");
		}else {
			log.info("Using SecurityContextHolderAwareRequestWrapper: The user does NOT have the necessary role to
			access this resource");
		}*/
        //This is yet another way to do the same, but using the request object injected to the method
        if (request.isUserInRole("ROLE_ADMIN")) {
            log.info("Using HttpServletRequest: The user has the necessary role to access this resource");
        } else {
            log.info("Using HttpServletRequest: The user does NOT have the necessary role to access this resource");
        }

        Pageable pageRequest = PageRequest.of(page, 3);
        Page<Client> clients = clientServiceImpl.findAll(pageRequest);
        PageRender<Client> render = new PageRender<>("/clients", clients);
        model.addAttribute(TITLE, messageSource.getMessage("text.list.title", null, locale));
        model.addAttribute("clients", clients);
        model.addAttribute("page", render);
        return "/list";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/form")
    public String create(Map<String, Object> model) {
        Client client = new Client();
        model.put(TITLE, "Client form");
        model.put(CLIENT, client);
        return FORM;
    }

    //@Secured("ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //The @PreAuthorize annotation is the same as @Secured, only it allows more control
    @RequestMapping(value = "/form/{id}")
    public String edit(@PathVariable Long id, Map<String, Object> model, RedirectAttributes flash) {
        if (id > 0) {
            Client client = clientServiceImpl.findOne(id);
            if (client != null) {
                model.put(TITLE, "Edit customer");
                model.put(CLIENT, client);
                return FORM;
            } else {
                flash.addFlashAttribute(ERROR, "The ID is not valid");
                return RDRECT_CLIENTS;
            }
        } else {
            flash.addFlashAttribute(ERROR, "The ID has to be positive");
            return RDRECT_CLIENTS;
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/form")
    public String save(@Valid Client client, BindingResult result, Model model,
                       @RequestParam("file") MultipartFile photo, RedirectAttributes flash,
                       SessionStatus sessionStatus) {
        if (result.hasErrors()) {
            model.addAttribute(TITLE, "Client form");
            return FORM;
        }
        if (!photo.isEmpty()) {
            /*
             * The following two lines could be used to
             * save the uploaded images to a folder inside
             * of the project structure, but this is
             * advised against. It is best to undock it and save
             * the files in an external folder.
             */
            //Path resources = Paths.get("src/main/resources/static/uploads");
            //String rootPath = resources.toFile().getAbsolutePath();
            //String rootPath = "C://Temp//uploads";

            //We add a "unique id" to the image name
            //to make sure they are not repeated

            //If the user already had a photo, we delete the old one
            if (client.getId() != null && client.getId() > 0
                && client.getPhoto() != null
                && !client.getPhoto().isEmpty()) {
                uploadFileServiceImpl.delete(client.getPhoto());
            }
            String uniqueFileName;
            uniqueFileName = uploadFileServiceImpl.copy(photo);
            flash.addFlashAttribute(
                    "info",
                    "Image uploaded successfully (" + uniqueFileName + ")");
            client.setPhoto(uniqueFileName);
        }
        String message = client.getId() != null ? "Customer edited successfully " : " Customer created successfully";
        clientServiceImpl.save(client);
        sessionStatus.setComplete();
        flash.addFlashAttribute("success", message);
        return "redirect:clients";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/remove/{id}")
    public String remove(@PathVariable Long id, RedirectAttributes flash, Map<String, Object> model) {
        if (id > 0) {
            Client client = clientServiceImpl.findOne(id);
            clientServiceImpl.delete(id);
            flash.addFlashAttribute("success", "Successfully removed customer");
            if (uploadFileServiceImpl.delete(client.getPhoto())) {
                flash.addFlashAttribute("info", "Foto " + client.getPhoto() + " successfully removed");
            }
        }
        return RDRECT_CLIENTS;
    }

    /*
     * This method allows you to have more control over the roles of the user, being able to access each of them
     */
    private boolean hasRole() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            Authentication auth = context.getAuthentication();
            if (auth != null) {
                Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
				/*for(GrantedAuthority authority : authorities) {
					if(role.equals(authority.getAuthority())) {
						return true;
					}
				}*/
                return authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));    //This form is more concise than
                // using the for
            }
        }
        return false;
    }
}
