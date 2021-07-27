package io.selenium.util.pages.context;

import io.selenium.utils.WebContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class DepartmentsTab extends WebContext {

    @FindBy(xpath = "//li")
    private List<Department> departments;

    @FindBy(xpath = "//li")
    private List<DepartmentInvalid> invaliDepartments;

    @FindBy(xpath = ".//li")
    private List<NoContext> noContexts;

    @FindBy(xpath = ".//li")
    private NoContext noContext;

    public List<Department> getDepartments() {
        return departments;
    }

    public List<DepartmentInvalid> getInvaliDepartments() {
        return invaliDepartments;
    }

    public void setInvaliDepartments(List<DepartmentInvalid> invaliDepartments) {
        this.invaliDepartments = invaliDepartments;
    }

    public List<NoContext> getNoContexts() {
        return noContexts;
    }

    public NoContext getNoContext() {
        return noContext;
    }

    public static class Department extends WebContext {
        @FindBy(xpath = ".//app-department")
        private List<Content> contents;

        public List<Content> getContents() {
            return contents;
        }

        public static class Content extends WebContext {
            @FindBy(xpath = ".//button")
            private WebElement button;

            public WebElement getButton() {
                return button;
            }
        }
    }

    public static class DepartmentInvalid extends WebContext {
        @FindBy(xpath = ".//app-department")
        private List<ContentInvalid> contents;

        public List<ContentInvalid> getContents() {
            return contents;
        }

        public static class ContentInvalid extends WebContext {
            public ContentInvalid(String s) {

            }
        }
    }

    public static class NoContext {

    }
}
