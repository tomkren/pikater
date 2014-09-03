<!-- title: GitHub overview -->

This project extends the [Pikater](https://github.com/peskk3am/pikater4) project with multi-user support, web based GUI and distributed infrastructure to allow simultaneous computation of many individual tasks.

As such, this application consists of 3 main parts:

1. **Core system**  
The original pikater project. Although it has been rewritten to support the extension, it still works as a standalone unit.
2. **Database framework**
3. **Web application extension**

The project also contains an inline documentation written in markdown, easily available as a wiki.
More information on each of these "components" can be found in the [documentation](#docs).




## Main features

Client:
* 2D editor to define experiments (boxes and edges style).
* Potentially many experiments may be scheduled to execution.
* Displaying the experiment results and converting/downloading them into a human-readable format, such as CSV.
* Uploading custom data sets and computation methods.

Server:
* Many features of the original Pikater project, such as computation method recommendation.
* Experiments planning and execution.
* Saving of trained models which can then be used in further experiments.
* Administrator functions, such as supervision of all scheduled experiments.


## Life-cycle

[[include:docs/guide_admin/lifecycle]]

## Wiki

This project contains inline documentation written in markdown, easily available as a wiki.

### Installation

1. Clone the `Eclipse-Vaadin-project` branch.
2. [[How to install|docs/guide_admin/wiki/01-Installation]]

### Launching

[[How to launch|docs/guide_admin/wiki/02-Launching]]

### Usage

[[How to use|docs/guide_admin/wiki/03-Usage]]




## Documentation<a name="docs"/>

[[Open documentation overview|Overview]]
