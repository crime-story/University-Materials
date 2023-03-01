void miscad(void)
{
	if (i > -1 && i <= 400 && j <= 750)
	{
		i = i + alpha;
		alpha = +step;
	}
	if (j > -1.0 && j <= 150) {
		j = j + alpha2;
		alpha2 = +step2;
	}