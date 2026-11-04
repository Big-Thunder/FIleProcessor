package org.shubham.fileuploadstuff.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.shubham.fileuploadstuff.SessionFileService;
import org.shubham.fileuploadstuff.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class FileUploadController {
    private final SessionFileService sessionFileService;

    @Autowired
    public FileUploadController(SessionFileService sessionFileService){
        this.sessionFileService = sessionFileService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model, HttpServletRequest request){
        String sessionId = request.getSession(true).getId();
        model.addAttribute("files", sessionFileService.loadAll(sessionId).map(path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                "serveFile", path.getFileName().toString(), request).build().toUri().toString().replace("%20", " ")).collect(Collectors.toList()));

        return "index";
    }

    @GetMapping("/files/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName, HttpServletRequest request){
        Resource file = sessionFileService.loadAsResource(request.getSession(true).getId(), fileName);

        if(file == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""+file.getFilename()+"\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request){
        sessionFileService.store(request.getSession(true).getId(), file);
        redirectAttributes.addFlashAttribute("message","You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @PostMapping("/delete/{fileName:.+}")
    public String handleFileDelete(@PathVariable("fileName") String fileName, RedirectAttributes redirectAttributes, HttpServletRequest request){
        System.out.println("Deleting: " + fileName);

        sessionFileService.delete(fileName, request.getSession(true).getId());

        redirectAttributes.addFlashAttribute("message", "deleted file " + fileName);

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc, RedirectAttributes redirectAttributes) {
        System.out.println("Not Found");

        ResponseEntity<HttpClientErrorException.NotFound> res = new ResponseEntity<>(HttpStatusCode.valueOf(404));
        redirectAttributes.addFlashAttribute("message", "requested file not found");
        return ResponseEntity.notFound().location(URI.create("/")).build();
    }
}
