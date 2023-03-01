	if (j >= 150 && j <= 250 && angle <= 0.5) {
		angle += beta;
		l = l + alpha3;
		alpha3 = +step3;
	}
	
	if (l >= 200 && l <= 370 && angle >= 0.5) {
		j = j + alpha2;
		alpha2 = +step2;
		l = l + alpha3;
		alpha3 = +step2;
	}
	if (l >= 370 && angle <= 0.6 && angle >= 0) {
		angle -= beta;
		j = j + alpha2;
		alpha2 = +step3;
	}