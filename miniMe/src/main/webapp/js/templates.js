// This file was automatically generated from helloWorld2.soy.
// Please don't edit this file by hand.

if (typeof example == 'undefined') { var example = {}; }
if (typeof example.templates == 'undefined') { example.templates = {}; }


example.templates.welcome2 = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
console.log(output);
  output.append('<h1 id="greeting">', soy.$$escapeHtml(opt_data.greeting), '</h1>The year is ', soy.$$escapeHtml(opt_data.year));
  if (!opt_sb) return output.toString();
};
