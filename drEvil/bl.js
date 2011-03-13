function onSuccess (func)
{
    return function (err, arg)
    {
	if (err) {
	    console.log(err)
	}
	else {
	    if (func == null || func == undefined) {
		return null;
	    }
	    func(arg);
	}
    }
}

exports.onSuccess = onSuccess;
