# Library Management Project

Prerequisites:

Create schema with name of 'librarymanagement' in MySql. Run the following command in mysql command line

 - create schema librarymanagement

# For Services.:

 - git clone https://github.com/axd164330/Library.git

 - update password value of 'spring.datasource.password' in the application-dev.properties if different from 'admin'

- cd services

 - mvn install

 - mvn spring-boot: run


# For Front end:

 - git clone https://github.com/axd164330/Library.git

 - cd FrontEnd

 - npm install

 - npm run start
