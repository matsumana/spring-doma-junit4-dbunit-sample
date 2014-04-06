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

package info.matsumana.service;

import com.google.common.collect.Lists;
import info.matsumana.AppConfig;
import info.matsumana.entity.Employee;
import info.matsumana.repository.EmployeeRepository;
import info.matsumana.repository.EmployeeRepositoryImpl;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        AppConfig.class,
        EmployeeService.class,
        EmployeeRepositoryImpl.class})
public class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllのテスト() throws Exception {
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

        when(employeeRepository.findAll()).thenReturn(
                Lists.newArrayList(employee1, employee2));

        List<Employee> expected = Lists.newArrayList(employee1, employee2);
        List<Employee> actual = sut.findAll();

        assertThat(actual.size(), is(expected.size()));

        for (int i = 0; i < actual.size(); i++) {
            Employee act = actual.get(i);
            Employee exp = expected.get(i);
            assertThat(act.employeeId, is(exp.employeeId));
            assertThat(act.employeeName, is(exp.employeeName));
            assertThat(act.hiredate, is(exp.hiredate));
            assertThat(act.salary, is(exp.salary));
            assertThat(act.versionNo, is(exp.versionNo));
        }
    }
}
