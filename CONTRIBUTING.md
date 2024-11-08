# Contributing to Relativity Script Library

Thank you for your interest in contributing to the **Relativity Script Library**! We welcome all contributions that align with our standards for quality, consistency, and usability. Please review the following guidelines to ensure that your contributions meet our requirements.

## Table of Contents
1. [Getting Started](#getting-started)
2. [General Contribution Guidelines](#general-contribution-guidelines)
3. [Script Structure and XML Schema](#script-structure-and-xml-schema)
4. [T-SQL Coding Standards and Best Practices](#t-sql-coding-standards-and-best-practices)
5. [Testing and Validation](#testing-and-validation)
6. [Submitting Your Contribution](#submitting-your-contribution)
7. [Code of Conduct](#code-of-conduct)

## Getting Started
Before contributing, please ensure that:
- You have read through the project's `README.md`.
- You have experience working within the Relativity environment (Relativity Server or RelativityOne).
- You are familiar with the XML structure and schema used for Relativity scripts, as detailed below.

## General Contribution Guidelines
- **Script Naming and Documentation**: Provide a meaningful name, description, and category for each script in the XML. These details should be clear and accurately reflect the script’s function.
- **Testing**: Thoroughly test your script in a supported Relativity environment to ensure it performs as expected.
- **Documentation**: Each script should include documentation in the `scripts` directory README, explaining its purpose, functionality, inputs, outputs, and any important usage details.

## Script Structure and XML Schema
All scripts contributed to this library must adhere to a standardized XML format defined by `RelativityScriptSchema.xsd`. The following outlines the required and optional elements for each script:

### Required Elements
- **`<script>`**: Root element containing all other elements.
  - **`<name>`**: The script's name.  
  - **`<description>`**: A concise description of the script’s purpose.
  - **`<category>`**: Defines the category or type of task the script addresses.
  - **`<version>`**: The version of the script, following a `major.minor` format (e.g., `1.0`).
    - **Constraints**: Must be at least 3 characters in length and follow the pattern `[0-9]+\.[0-9]+`.
  - **`<action>`**: Contains the T-SQL code to be executed within Relativity.
    - **Occurrences**: 1 to 10 instances allowed.
    - **Attributes**:
      - `returns` (required): Specifies the return type, either `status` or `table`, with `table` preferred for scripts in this repository.
      - `timeout` (optional): Specifies the timeout, either `indefinite` or a numeric value.
      - `displaywarning` (optional): Boolean to show a warning.
      - `allowhtmltagsinoutput` (optional): Boolean to allow HTML tags in output, with `false` preferred for scripts in this repository.
      - `name` (optional): Name for the action.

### Optional Elements
In addition to the required elements, the following optional elements are available, each with specific attributes and nested elements:

- **`<input>`**: Defines input parameters for the script.
  - **Sub-elements** (up to 10 occurrences):
    - `<constant>`, `<sql>`, `<field>`, `<search>`, `<searchprovider>`, `<object>`
  - **Attributes** for `input` sub-elements vary, including:
    - `id` and `name` (both required for each sub-element)
    - `type` (for `<constant>` only): Must be one of `date`, `datetime`, `text`, `user`, `number`, or `timezone`.
    - `displaytype` (for `<object>` only): Specifies the display type as `singlepicker` or `multipicker`.
- **`<display>`**: Configures display settings for the script.
  - **Attributes**:
    - `type` (required): Either `report` or `table`.
  - **Nested element**:
    - `<settings>` with required attribute `reporttitle`.
- **`<key>`**: Defines an optional key identifier.
- **`<security>`**: Defines security access controls for the script.
  - **Sub-elements**:
    - `<acl>`: Access control list specifying permissions.
      - Attributes include `id`, `typeartifactid`, `typeartifactguid`, and `type` (view, edit, delete).

Please refer to the `RelativityScriptSchema.xsd` file for additional details on required element structures and constraints.

## T-SQL Coding Standards and Best Practices
When adding SQL code within `<action>` or `<sql>` elements, please follow these best practices to ensure readability, performance, and security.

### 1. Formatting and Readability
- **Indentation**: Use a clear indentation style, similar to the following example:
  ```sql
  SELECT TOP (5)
      [d].[ControlNumber],
      [c].[Name] AS [Responsive Designation]
  FROM [EDDSDBO].[Document] AS [d]
  INNER JOIN [EDDSDBO].[ZCodeArtifact_1000041] AS [z]
      ON [d].[ArtifactID] = [z].[AssociatedArtifactID]
  INNER JOIN [EDDSDBO].[Code] AS [c]
      ON [z].[CodeArtifactID]= [c].[ArtifactID]```
- **Capitalization**: SQL keywords (e.g., `SELECT`, `JOIN`, `ON`, `WHERE`) should be written in uppercase to improve readability.
- **Complex Logic**: Encapsulate explanations of complex logic within multi-line comment blocks (`/* complex logic explanation */`).

### 2. Performance Optimization
- **Avoid `SELECT *`**: Specify required columns explicitly rather than using `SELECT *`.
- **Efficient Searches**: When using `<search>` inputs, select only `[Document].[ArtifactID]` and store results in a temp table or table variable, working with it instead of repeatedly referencing the search input.
- **Batch Processing**: For `INSERT`, `UPDATE`, or `DELETE` statements, process records in batches of 10,000 to limit database load and improve readability.

### 3. Error Handling and Safety
- **Graceful Error Handling**: Where possible, handle errors gracefully and report them meaningfully to the end user.
- **Safe Transactions**: Use transactions to maintain data integrity when necessary and avoid long-running, blocking operations.

### 4. Security and Data Protection
- **Avoid Direct Text Inputs**: Avoid using direct text inputs for `<constant>` types (without the `<option>` sub-elements) to reduce the risk of SQL injection.
- **Permission Validation**: Use the `<security>` element to check permissions on securable Relativity objects to ensure users have the correct access rights.

### 5. Compatibility
- **SQL Server Version**: Use only features and functions supported by the oldest SQL Server version compatible with Relativity Server (currently SQL Server 2017).

## Testing and Validation
All scripts must pass both functional testing and XML schema validation before submission.

1. **Functional Testing**: Ensure that your script works as intended within a Relativity environment (Relativity Server or RelativityOne).
2. **Schema Validation**: All scripts are validated against the `RelativityScriptSchema.xsd` file through GitHub Actions. Validation checks for compliance with the XML structure and attributes. Any validation errors must be resolved before the script can be merged.

## Submitting Your Contribution
When your script is ready to be submitted:

1. **Fork the Repository**: Fork this repository and make your changes in a separate branch.
2. **Create a Pull Request (PR)**: Submit a pull request to the main repository, including:
   - A description of the script's purpose and any additional notes.
   - Confirmation that you have tested the script in a Relativity environment.
3. **Address Review Feedback**: The maintainers will review your PR and may request modifications. Please address any feedback promptly to ensure a smooth review process.

## Code of Conduct
We are committed to providing a welcoming and respectful environment for all contributors. Please review and adhere to our [Code of Conduct](CODE_OF_CONDUCT.md) when contributing.

---

Thank you for contributing to the Relativity Script Library! We appreciate your support in building a valuable resource for the Relativity community.
