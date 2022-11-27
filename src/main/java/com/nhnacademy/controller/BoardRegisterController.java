package com.nhnacademy.controller;

import com.nhnacademy.domain.Board;
import com.nhnacademy.domain.BoardRegisterRequest;
import com.nhnacademy.domain.UserVO;
import com.nhnacademy.exception.NotAcceptableFileTypeException;
import com.nhnacademy.exception.UserNotAllowedException;
import com.nhnacademy.exception.ValidationFailedException;
import com.nhnacademy.service.BoardService;
import com.nhnacademy.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardRegisterController {

    private static String UPLOAD_DIR = "/Users/gwanii/Downloads/";

    private static List<String> acceptableFileType =  List.of("image/gif","image/jpg","image/jpeg","image/png");

    private final BoardService boardService;
    private final FileService fileService;

    public BoardRegisterController(BoardService boardService, FileService fileService) {
        this.boardService = boardService;
        this.fileService = fileService;
    }

    @GetMapping("/register")
    public String getBoardRegisterForm() {
        return "/board/registerForm";
    }

    @PostMapping("/register")
    public String doBoardRegister(@Valid @ModelAttribute(value = "board") BoardRegisterRequest boardRequest,
                                  @RequestParam(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,
                                  BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        if (!fileEmptyCheck(uploadFiles)) {
            fileTypeCheck(uploadFiles);
        }

        boardService.registerBoard(boardRequest);

        if (!fileEmptyCheck(uploadFiles)) {
            fileUpload(uploadFiles, boardRequest.getId());
        }

        return "redirect:/";
    }

    @GetMapping("/modify/{boardId}")
    public String getModifyForm(@PathVariable(value = "boardId") long boardId,
                                @SessionAttribute(value = "user") UserVO userSession,
                                Model model) {

        if (!boardService.allowedUserCheck(boardId, userSession)) {
            throw new UserNotAllowedException();
        }

        Board board = boardService.findById(boardId);
        model.addAttribute("board", board);

        return "/board/modify";
    }

    @PostMapping("/modify/{boardId}")
    public String doModify(@PathVariable(value = "boardId") long boardId,
                           @Valid @ModelAttribute(value = "board") BoardRegisterRequest boardRequest,
                           @RequestParam(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,
                           @SessionAttribute(value = "user") UserVO userSession,
                           BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        if (!boardService.allowedUserCheck(boardId, userSession)) {
            throw new UserNotAllowedException();
        }

        if (!fileEmptyCheck(uploadFiles)) {
            fileTypeCheck(uploadFiles);
        }

        boardService.modifyBoard(
                boardId,
                boardRequest.getUserName(),
                boardRequest.getTitle(),
                boardRequest.getContent()
        );

        fileService.deleteFileByBoardId(boardId);

        if (!fileEmptyCheck(uploadFiles)) {
            fileUpload(uploadFiles, boardId);
        }

        return "redirect:/";
    }


    private void fileUpload(MultipartFile[] uploadFiles, long boardId) throws IOException {
        for (MultipartFile file : uploadFiles) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            fileService.insertFile(boardId, "user", fileName);
            file.transferTo(Paths.get(UPLOAD_DIR + fileName));
        }
    }

    private boolean fileEmptyCheck(MultipartFile[] uploadFiles) {
        for (MultipartFile file : uploadFiles) {
            if (file.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void fileTypeCheck(MultipartFile[] uploadFiles) {
        for (MultipartFile file : uploadFiles) {
            if (!acceptableFileType.contains(file.getContentType())) {
                throw new NotAcceptableFileTypeException();
            }
        }
    }
}
