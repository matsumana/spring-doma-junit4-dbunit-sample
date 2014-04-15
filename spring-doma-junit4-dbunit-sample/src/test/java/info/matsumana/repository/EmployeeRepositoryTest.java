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

package info.matsumana.repository;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import info.matsumana.AppConfig;
import info.matsumana.entity.Employee;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunitng.dataset.BeanListConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.xml.sax.InputSource;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.File;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        AppConfig.class,
        EmployeeRepositoryImpl.class})
@TransactionConfiguration
@Transactional
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class})
public class EmployeeRepositoryTest {

    @Inject
    EmployeeRepository sut;

    /**
     * 引数で受け取ったクラスのclassファイルがある物理ディレクトリパスを取得する
     *
     * @param clazz クラス
     * @return 物理ディレクトリパス
     */
    String resolvePhysicalDirectory(Class clazz) {
        return new File(clazz.getResource("").getFile()).getPath() + File.separator;
    }

    @Test
    @DatabaseSetup("EmployeeRepositoryTest_findAll_000_data.xml")
    public void findAllのテスト() throws Exception {

        // 期待値
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        String expedtedFile = "EmployeeRepositoryTest_findAll_000_expected.xml";
        IDataSet expectedDataSet = builder.build(
                new InputSource(resolvePhysicalDirectory(getClass()) + expedtedFile));

        // テスト対象メソッド実行
        List<Employee> actual = sut.findAll();

        // DataSetに変換
        IDataSet actualdDataSet = new BeanListConverter(actual).convert();

        // assert
        Assertion.assertEquals(expectedDataSet, actualdDataSet);
    }

    @Test
//    @Rollback(false)  // これを書くとコミットされる （assert failでもコミットされるので注意）
    @DatabaseSetup("EmployeeRepositoryTest_findAll_000_data.xml")
    @ExpectedDatabase(value = "EmployeeRepositoryTest_delete_000_expected.xml", table = "employee",
            query = "select * from employee order by employee_id")
    public void deleteのテスト() throws Exception {

        // 削除対象を取得
        Employee employee = sut.findById(4);

        // テスト対象メソッド実行
        sut.delete(employee);
    }
}
