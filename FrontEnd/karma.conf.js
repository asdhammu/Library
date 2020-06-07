// Karma configuration file, see link for more information
// https://karma-runner.github.io/1.0/config/configuration-file.html

module.exports = function (config) {
  config.set({
    basePath: '',
    frameworks: ['jasmine', '@angular-devkit/build-angular'],
    plugins: [
      require('karma-jasmine'),
      require('karma-chrome-launcher'),
      require('karma-jasmine-html-reporter'),
      require('karma-coverage-istanbul-reporter'),
      require('@angular-devkit/build-angular/plugins/karma'),
      require('karma-junit-reporter')
    ],
    client: {
      clearContext: false // leave Jasmine Spec Runner output visible in browser
    },
    coverageIstanbulReporter: {
      dir: require('path').join(__dirname, '../coverage'),
      reports: ['html', 'lcovonly', 'text-summary'],
      fixWebpackSourcePaths: true
    },
    reporters: ['progress', 'kjhtml', 'junit'],
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
    browsers: ['Chrome'],
    singleRun: false,
    junitReporter: {
      outputDir: require('path').join(__dirname, '../junit'), // results will be saved as $outputDir/$browserName.xml
      outputFile: 'test-results.xml', // if included, results will be saved as $outputDir/$browserName/$outputFile
      useBrowserName: false,
      suite: '', // suite will become the package name attribute in xml testsuite element
      // nameFormatter: undefined, // function (browser, result) to customize the name attribute in xml testcase element
      // classNameFormatter: undefined, // function (browser, result) to customize the classname attribute in xml testcase element
      xmlVersion: null // use '1' if reporting to be per SonarQube 6.2 XML format
    }
  });
};
