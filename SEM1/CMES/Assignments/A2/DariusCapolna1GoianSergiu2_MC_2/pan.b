	switch (t->back) {
	default: Uerror("bad return move");
	case  0: goto R999; /* nothing to undo */

		 /* CLAIM ltl3 */
;
		;
		;
		;
		
	case 5: /* STATE 11 */
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* CLAIM never_0 */
;
		;
		;
		;
		
	case 8: /* STATE 11 */
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* PROC Controller */

	case 9: /* STATE 1 */
		;
		XX = 1;
		unrecv(now.sensor_to_controller, XX-1, 0, ((P1 *)this)->_5_signal, 1);
		((P1 *)this)->_5_signal = trpt->bup.oval;
		;
		;
		goto R999;

	case 10: /* STATE 4 */
		;
		now.lightIsOn = trpt->bup.ovals[1];
	/* 0 */	((P1 *)this)->_5_signal = trpt->bup.ovals[0];
		;
		;
		ungrab_ints(trpt->bup.ovals, 2);
		goto R999;

	case 11: /* STATE 5 */
		;
		_m = unsend(now.controller_to_sensor);
		;
		goto R999;
;
		
	case 12: /* STATE 10 */
		goto R999;

	case 13: /* STATE 8 */
		;
		now.lightIsOn = trpt->bup.ovals[1];
	/* 0 */	((P1 *)this)->_5_signal = trpt->bup.ovals[0];
		;
		;
		ungrab_ints(trpt->bup.ovals, 2);
		goto R999;
;
		;
		
	case 15: /* STATE 13 */
		;
		_m = unsend(now.controller_to_sensor);
		;
		goto R999;

	case 16: /* STATE 14 */
		;
	/* 0 */	((P1 *)this)->_5_signal = trpt->bup.ovals[1];
		XX = 1;
		unrecv(now.sensor_to_controller, XX-1, 0, ((P1 *)this)->_5_signal, 1);
		((P1 *)this)->_5_signal = trpt->bup.ovals[0];
		;
		;
		ungrab_ints(trpt->bup.ovals, 2);
		goto R999;

	case 17: /* STATE 20 */
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* PROC Sensor */

	case 18: /* STATE 2 */
		;
		now.personPresent = trpt->bup.oval;
		;
		goto R999;

	case 19: /* STATE 4 */
		;
		_m = unsend(now.sensor_to_controller);
		;
		goto R999;

	case 20: /* STATE 5 */
		;
		XX = 1;
		unrecv(now.controller_to_sensor, XX-1, 0, 2, 1);
		;
		;
		goto R999;
;
		
	case 21: /* STATE 8 */
		goto R999;

	case 22: /* STATE 10 */
		;
		_m = unsend(now.sensor_to_controller);
		;
		goto R999;

	case 23: /* STATE 11 */
		;
		now.personPresent = trpt->bup.oval;
		;
		goto R999;

	case 24: /* STATE 13 */
		;
		XX = 1;
		unrecv(now.controller_to_sensor, XX-1, 0, 1, 1);
		;
		;
		goto R999;
;
		;
		
	case 26: /* STATE 16 */
		;
		_m = unsend(now.sensor_to_controller);
		;
		goto R999;
;
		;
		
	case 28: /* STATE 18 */
		;
		_m = unsend(now.sensor_to_controller);
		;
		goto R999;

	case 29: /* STATE 25 */
		;
		p_restor(II);
		;
		;
		goto R999;
	}

