package br.ufpa.castanhal.es2.vacinacao;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("br.ufpa.castanhal.es2.vacinacao");

        noClasses()
            .that()
            .resideInAnyPackage("br.ufpa.castanhal.es2.vacinacao.service..")
            .or()
            .resideInAnyPackage("br.ufpa.castanhal.es2.vacinacao.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..br.ufpa.castanhal.es2.vacinacao.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
