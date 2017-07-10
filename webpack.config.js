var path = require("path");
const ExtractTextPlugin = require('extract-text-webpack-plugin');

const extractSass = new ExtractTextPlugin({
    filename: "[name].css"
});


module.exports = {
  entry: {
    app: [
      './app/frontend/index.js'
    ]
  },

  output: {
    path: path.resolve(__dirname + '/public/javascripts'),
    filename: '[name].js',
    publicPath: '/'
  },

  module: {
    rules: [
      {
        test: /\.css$/,
        use: [
          'style-loader',
          'css-loader'
        ]
      },
      {
          test: /\.scss$/,
          exclude: /node_modules/,
          // loader: ExtractTextPlugin.extract("style", "css?sourceMap!postcss!sass?sourceMap&outputStyle=expanded")
          use: extractSass.extract({
              fallback: "style-loader",
              use: [{
                loader: 'css-loader'
              }, {
                loader: 'sass-loader',
                options: {
                  includePaths: [path.resolve(__dirname, "node_modules")]
                }
              }],
              publicPath: 'build'
          })
      },
      {
        test:    /\.html$/,
        exclude: /node_modules/,
        loader:  'file-loader?name=[name].[ext]',
      },
      {
        test:    /\.elm$/,
        exclude: [/elm-stuff/, /node_modules/],
        loader:  'elm-webpack-loader?verbose=true&warn=true&debug=true',
      },
      {
        test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        loader: 'url-loader?limit=10000&mimetype=application/font-woff',
      },
      {
        test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        loader: 'file-loader',
      },
    ],

    noParse: /\.elm$/,
  },
  plugins: [
   extractSass
   ],
  devServer: {
    inline: true,
    historyApiFallback: true,
    stats: { colors: true },
  }


};
