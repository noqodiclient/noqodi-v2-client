const path = require('path')

var config = {
    // TODO: Add common Configuration
    module: {},
};

var commandConfig = Object.assign({}, config, {
  devtool: 'nosources-source-map',
  entry: './src/main/resources/static/resources/js/src/command-ui-util.js',
  output: {
    filename: 'noqodi-command-core.js',
    path: path.resolve(__dirname, './src/main/resources/static/resources/js/lib')
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /(node_modules|bower_components)/,
        use: {
          loader: 'babel-loader',
        }
      }
    ]
  }
});
var paymentConfig = Object.assign({}, config,{
  devtool: 'nosources-source-map',
  entry: './src/main/resources/static/resources/js/src/payment-ui-util.js',
  output: {
    filename: 'noqodi-payment-core.js',
    path: path.resolve(__dirname, './src/main/resources/static/resources/js/lib')
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /(node_modules|bower_components)/,
        use: {
          loader: 'babel-loader',
        }
      }
    ]
  }
});

// Return Array of Configurations
module.exports = [
    commandConfig, paymentConfig,       
];