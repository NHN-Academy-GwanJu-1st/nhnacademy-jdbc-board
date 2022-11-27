package com.nhnacademy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DownloadControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DownloadController()).build();
    }

    @Test
    void doDownload_success() throws Exception {

        String fileName = "페페_img.png";

        mockMvc.perform(get("/download")
                        .param("filename", fileName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentType()).isEqualTo("image/png"));
    }

    @Test
    void doDownload_notExistFile_thenFileNotFoundException() throws Exception {

        String fileName = "notExistFile.png";

        Throwable th = catchThrowable(() ->
                mockMvc.perform(get("/download")
                                .param("filename", fileName))
                        .andDo(print())
        );

        assertThat(th).isInstanceOf(FileNotFoundException.class);
    }

}