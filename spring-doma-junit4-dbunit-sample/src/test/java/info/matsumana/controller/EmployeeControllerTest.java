/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Manabu Matsuzaki
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package info.matsumana.controller;

import com.google.common.collect.Lists;
import info.matsumana.AppConfig;
import info.matsumana.entity.Employee;
import info.matsumana.repository.EmployeeRepositoryImpl;
import info.matsumana.service.EmployeeService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        AppConfig.class,
        EmployeeController.class,
        EmployeeService.class,
        EmployeeRepositoryImpl.class})
public class EmployeeControllerTest {

    MockMvc mockMvc;

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    EmployeeController sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(sut).build();
    }

    @Test
    public void コントローラのテスト() throws Exception {
        Employee employee1 = new Employee();
        employee1.employeeId = 1;
        employee1.employeeName = "ほげ";
        employee1.hiredate = DateTime.parse("2014-02-01").toDate();
        employee1.salary = new BigDecimal("2000");
        employee1.versionNo = 10;

        Employee employee2 = new Employee();
        employee2.employeeId = 4;
        employee2.employeeName = "ふが";
        employee2.hiredate = DateTime.parse("2006-05-01").toDate();
        employee2.salary = new BigDecimal("1000");
        employee2.versionNo = 1;

        when(employeeService.findAll()).thenReturn(
                Lists.newArrayList(employee1, employee2));

        mockMvc.perform(get("/api/v1/employee"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))

                .andExpect(jsonPath("$[0].employeeId").value(1))
                .andExpect(jsonPath("$[0].employeeName").value("ほげ"))
                .andExpect(jsonPath("$[0].hiredate").value(1391180400000L))
                .andExpect(jsonPath("$[0].salary").value(2000))
                .andExpect(jsonPath("$[0].versionNo").value(10))

                .andExpect(jsonPath("$[1].employeeId").value(4))
                .andExpect(jsonPath("$[1].employeeName").value("ふが"))
                .andExpect(jsonPath("$[1].hiredate").value(1146409200000L))
                .andExpect(jsonPath("$[1].salary").value(1000))
                .andExpect(jsonPath("$[1].versionNo").value(1));
    }
}
