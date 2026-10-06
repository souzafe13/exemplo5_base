package br.senac.sp.tads.dsw.exemplo5.controller;

import br.senac.sp.tads.dsw.exemplo5.model.Departamento;
import br.senac.sp.tads.dsw.exemplo5.repository.DepartamentoRepository;

import java.lang.runtime.ObjectMethods;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional

public class DepartamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartamentoRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarDepartamentocomSucesso() throws Exception {

        // 1ª etapa: criar um objeto que queremos enviar

        Departamento departamento = new Departamento();
        departamento.setNome("Turma B - TADS");
        departamento.setOrcamento(360.00);

        // 2ª etapa: converter o objeto JAVA para JSON (tring)
        String json = objectMapper.writeValueAsString(departamento);

        // 3ªetapa: enviar o POST para a API
        mockMvc.perform(

                post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                // 4ª etapa: verificar se o retorno está correto
                .andExpect(status().isCreated()) // 201 - Created
                .andExpect(jsonPath("$.id").exists()) // verificar se o campo id existe
                .andExpect(jsonPath("$.nome").value("Turma B - TADS")) // verificar se o nome está correto
                .andExpect(jsonPath("$.orcamento").value(360.00)); // verificar se o orçamento está correto

    }

    @Test
    void deveListarTodosOsDepartamentos() throws Exception {
        Departamento departamento = new Departamento();
        departamento.setNome("Turma B - TADS");
        departamento.setOrcamento(360.00);
        repository.save(departamento);

        mockMvc.perform(get("/api/departamentos"))
                .andExpect(status().isOk()); 
    }

    @Test 
    void deveBuscarDepartamentoPorId() throws Exception {
        Departamento departamento = new Departamento();
        departamento.setNome("Turma B - TADS");
        departamento.setOrcamento(360.00);
        repository.save(departamento);

        mockMvc.perform(get("/api/departamentos/" + departamento.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Turma B - TADS"))
                .andExpect(jsonPath("$.orcamento").value(360.00));
    }
}