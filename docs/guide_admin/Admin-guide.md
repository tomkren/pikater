<!-- --- title: Admin guide -->

[[_TOC_]]

## How to install, deploy, launch & maintain

[[include:lifecycle]]

## Responsibilities for administrators

To see a list of what administrators can (and should) do within the GUI, refer to [[web application documentation|Web-documentation#adminFeatures]].
To learn how these features can be exercised, refer to [[user guide|User-guide]], particularly to [[default page|User-guide#defaultPage]].

## Logging

The application doesn't use unified logging technology, implementation or interface at this moment. Individual application components use their own. They are all defined in the `org.pikater.shared.logging` package. Each application component has its own subpackage defined.

An attempt has been made to centralize logging into the servlet container but has not been fully implemented/used after all. For more information, refer to [[default package description|Default-package-description]].
