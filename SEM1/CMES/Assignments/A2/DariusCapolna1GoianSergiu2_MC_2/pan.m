#define rand	pan_rand
#if defined(HAS_CODE) && defined(VERBOSE)
	cpu_printf("Pr: %d Tr: %d\n", II, t->forw);
#endif
	switch (t->forw) {
	default: Uerror("bad forward move");
	case 0:	/* if without executable clauses */
		continue;
	case 1: /* generic 'goto' or 'skip' */
		IfNotBlocked
		_m = 3; goto P999;
	case 2: /* generic 'else' */
		IfNotBlocked
		if (trpt->o_pm&1) continue;
		_m = 3; goto P999;

		 /* CLAIM ltl3 */
	case 3: /* STATE 1 - _spin_nvr.tmp:3 - [(!(((personPresent&&lightIsOn)||(!(personPresent)&&!(lightIsOn)))))] (0:0:0 - 1) */
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported1 = 0; int nn = (int) ((Pclaim *)this)->_n;
			if (verbose && !reported1)
			{	printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#else
{	static int reported1 = 0;
			if (verbose && !reported1)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[3][1] = 1;
		if (!( !(((((int)now.personPresent)&&((int)now.lightIsOn))||( !(((int)now.personPresent))&& !(((int)now.lightIsOn)))))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 4: /* STATE 7 - _spin_nvr.tmp:8 - [(!(((personPresent&&lightIsOn)||(!(personPresent)&&!(lightIsOn)))))] (0:0:0 - 1) */
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported7 = 0; int nn = (int) ((Pclaim *)this)->_n;
			if (verbose && !reported7)
			{	printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported7 = 1;
				fflush(stdout);
		}	}
#else
{	static int reported7 = 0;
			if (verbose && !reported7)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported7 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[3][7] = 1;
		if (!( !(((((int)now.personPresent)&&((int)now.lightIsOn))||( !(((int)now.personPresent))&& !(((int)now.lightIsOn)))))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 5: /* STATE 11 - _spin_nvr.tmp:10 - [-end-] (0:0:0 - 1) */
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported11 = 0; int nn = (int) ((Pclaim *)this)->_n;
			if (verbose && !reported11)
			{	printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported11 = 1;
				fflush(stdout);
		}	}
#else
{	static int reported11 = 0;
			if (verbose && !reported11)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported11 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[3][11] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* CLAIM never_0 */
	case 6: /* STATE 1 - D:\UBB_Master_DistributedSystemsInInternet\SEM1\CMES\Assignments\A2\DariusCapolna1GoianSergiu2_MC_2\smart_lighting.ltl:4 - [(((!((!(personPresent)&&!(lightIsOn)))&&!(((personPresent&&lightIsOn)||(!(personPresent)&&!(lightIsOn)))))&&!((personPresent&&lightIsOn))))] (0:0:0 - 1) */
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported1 = 0; int nn = (int) ((Pclaim *)this)->_n;
			if (verbose && !reported1)
			{	printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#else
{	static int reported1 = 0;
			if (verbose && !reported1)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[2][1] = 1;
		if (!((( !(( !(((int)now.personPresent))&& !(((int)now.lightIsOn))))&& !(((((int)now.personPresent)&&((int)now.lightIsOn))||( !(((int)now.personPresent))&& !(((int)now.lightIsOn))))))&& !((((int)now.personPresent)&&((int)now.lightIsOn))))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 7: /* STATE 7 - D:\UBB_Master_DistributedSystemsInInternet\SEM1\CMES\Assignments\A2\DariusCapolna1GoianSergiu2_MC_2\smart_lighting.ltl:9 - [(!(((personPresent&&lightIsOn)||(!(personPresent)&&!(lightIsOn)))))] (0:0:0 - 1) */
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported7 = 0; int nn = (int) ((Pclaim *)this)->_n;
			if (verbose && !reported7)
			{	printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported7 = 1;
				fflush(stdout);
		}	}
#else
{	static int reported7 = 0;
			if (verbose && !reported7)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported7 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[2][7] = 1;
		if (!( !(((((int)now.personPresent)&&((int)now.lightIsOn))||( !(((int)now.personPresent))&& !(((int)now.lightIsOn)))))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 8: /* STATE 11 - D:\UBB_Master_DistributedSystemsInInternet\SEM1\CMES\Assignments\A2\DariusCapolna1GoianSergiu2_MC_2\smart_lighting.ltl:11 - [-end-] (0:0:0 - 1) */
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported11 = 0; int nn = (int) ((Pclaim *)this)->_n;
			if (verbose && !reported11)
			{	printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported11 = 1;
				fflush(stdout);
		}	}
#else
{	static int reported11 = 0;
			if (verbose && !reported11)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)this)->_p, src_claim[ (int) ((Pclaim *)this)->_p ]);
				reported11 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[2][11] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* PROC Controller */
	case 9: /* STATE 1 - smart_lighting.pml:49 - [sensor_to_controller?signal] (0:0:1 - 1) */
		reached[1][1] = 1;
		if (boq != now.sensor_to_controller) continue;
		if (q_len(now.sensor_to_controller) == 0) continue;

		XX=1;
		(trpt+1)->bup.oval = ((P1 *)this)->_5_signal;
		;
		((P1 *)this)->_5_signal = qrecv(now.sensor_to_controller, XX-1, 0, 1);
#ifdef VAR_RANGES
		logval("Controller:signal", ((P1 *)this)->_5_signal);
#endif
		;
		
#ifdef HAS_CODE
		if (readtrail && gui) {
			char simtmp[32];
			sprintf(simvals, "%d?", now.sensor_to_controller);
		sprintf(simtmp, "%d", ((P1 *)this)->_5_signal); strcat(simvals, simtmp);		}
#endif
		if (q_zero(now.sensor_to_controller))
		{	boq = -1;
#ifndef NOFAIR
			if (fairness
			&& !(trpt->o_pm&32)
			&& (now._a_t&2)
			&&  now._cnt[now._a_t&1] == II+2)
			{	now._cnt[now._a_t&1] -= 1;
#ifdef VERI
				if (II == 1)
					now._cnt[now._a_t&1] = 1;
#endif
#ifdef DEBUG
			printf("%3d: proc %d fairness ", depth, II);
			printf("Rule 2: --cnt to %d (%d)\n",
				now._cnt[now._a_t&1], now._a_t);
#endif
				trpt->o_pm |= (32|64);
			}
#endif

		};
		_m = 4; goto P999; /* 0 */
	case 10: /* STATE 2 - smart_lighting.pml:51 - [((signal==personDetected))] (5:0:2 - 1) */
		IfNotBlocked
		reached[1][2] = 1;
		if (!((((P1 *)this)->_5_signal==4)))
			continue;
		/* dead 1: _5_signal */  (trpt+1)->bup.ovals = grab_ints(2);
		(trpt+1)->bup.ovals[0] = ((P1 *)this)->_5_signal;
#ifdef HAS_CODE
		if (!readtrail)
#endif
			((P1 *)this)->_5_signal = 0;
		/* merge: printf('Controller: Turning light ON\\n')(5, 3, 5) */
		reached[1][3] = 1;
		Printf("Controller: Turning light ON\n");
		/* merge: lightIsOn = 1(5, 4, 5) */
		reached[1][4] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.lightIsOn);
		now.lightIsOn = 1;
#ifdef VAR_RANGES
		logval("lightIsOn", ((int)now.lightIsOn));
#endif
		;
		_m = 3; goto P999; /* 2 */
	case 11: /* STATE 5 - smart_lighting.pml:54 - [controller_to_sensor!acknowledgeSensor] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][5] = 1;
		if (q_len(now.controller_to_sensor))
			continue;
#ifdef HAS_CODE
		if (readtrail && gui) {
			char simtmp[32];
			sprintf(simvals, "%d!", now.controller_to_sensor);
		sprintf(simtmp, "%d", 2); strcat(simvals, simtmp);		}
#endif
		
		qsend(now.controller_to_sensor, 0, 2, 1);
		{ boq = now.controller_to_sensor; };
		_m = 2; goto P999; /* 0 */
	case 12: /* STATE 10 - smart_lighting.pml:60 - [.(goto)] (0:17:0 - 2) */
		IfNotBlocked
		reached[1][10] = 1;
		;
		/* merge: .(goto)(0, 18, 17) */
		reached[1][18] = 1;
		;
		_m = 3; goto P999; /* 1 */
	case 13: /* STATE 6 - smart_lighting.pml:56 - [((signal==roomEmpty))] (17:0:2 - 1) */
		IfNotBlocked
		reached[1][6] = 1;
		if (!((((P1 *)this)->_5_signal==3)))
			continue;
		/* dead 1: _5_signal */  (trpt+1)->bup.ovals = grab_ints(2);
		(trpt+1)->bup.ovals[0] = ((P1 *)this)->_5_signal;
#ifdef HAS_CODE
		if (!readtrail)
#endif
			((P1 *)this)->_5_signal = 0;
		/* merge: printf('Controller: Turning light OFF\\n')(17, 7, 17) */
		reached[1][7] = 1;
		Printf("Controller: Turning light OFF\n");
		/* merge: lightIsOn = 0(17, 8, 17) */
		reached[1][8] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.lightIsOn);
		now.lightIsOn = 0;
#ifdef VAR_RANGES
		logval("lightIsOn", ((int)now.lightIsOn));
#endif
		;
		/* merge: .(goto)(17, 10, 17) */
		reached[1][10] = 1;
		;
		/* merge: .(goto)(0, 18, 17) */
		reached[1][18] = 1;
		;
		_m = 3; goto P999; /* 4 */
	case 14: /* STATE 12 - smart_lighting.pml:63 - [(lightIsOn)] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][12] = 1;
		if (!(((int)now.lightIsOn)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 15: /* STATE 13 - smart_lighting.pml:64 - [controller_to_sensor!requestStatus] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][13] = 1;
		if (q_len(now.controller_to_sensor))
			continue;
#ifdef HAS_CODE
		if (readtrail && gui) {
			char simtmp[32];
			sprintf(simvals, "%d!", now.controller_to_sensor);
		sprintf(simtmp, "%d", 1); strcat(simvals, simtmp);		}
#endif
		
		qsend(now.controller_to_sensor, 0, 1, 1);
		{ boq = now.controller_to_sensor; };
		_m = 2; goto P999; /* 0 */
	case 16: /* STATE 14 - smart_lighting.pml:65 - [sensor_to_controller?signal] (17:0:2 - 1) */
		reached[1][14] = 1;
		if (boq != now.sensor_to_controller) continue;
		if (q_len(now.sensor_to_controller) == 0) continue;

		XX=1;
		(trpt+1)->bup.ovals = grab_ints(2);
		(trpt+1)->bup.ovals[0] = ((P1 *)this)->_5_signal;
		;
		((P1 *)this)->_5_signal = qrecv(now.sensor_to_controller, XX-1, 0, 1);
#ifdef VAR_RANGES
		logval("Controller:signal", ((P1 *)this)->_5_signal);
#endif
		;
		
#ifdef HAS_CODE
		if (readtrail && gui) {
			char simtmp[32];
			sprintf(simvals, "%d?", now.sensor_to_controller);
		sprintf(simtmp, "%d", ((P1 *)this)->_5_signal); strcat(simvals, simtmp);		}
#endif
		if (q_zero(now.sensor_to_controller))
		{	boq = -1;
#ifndef NOFAIR
			if (fairness
			&& !(trpt->o_pm&32)
			&& (now._a_t&2)
			&&  now._cnt[now._a_t&1] == II+2)
			{	now._cnt[now._a_t&1] -= 1;
#ifdef VERI
				if (II == 1)
					now._cnt[now._a_t&1] = 1;
#endif
#ifdef DEBUG
			printf("%3d: proc %d fairness ", depth, II);
			printf("Rule 2: --cnt to %d (%d)\n",
				now._cnt[now._a_t&1], now._a_t);
#endif
				trpt->o_pm |= (32|64);
			}
#endif

		};
		/* dead 2: _5_signal */  (trpt+1)->bup.ovals[1] = ((P1 *)this)->_5_signal;
#ifdef HAS_CODE
		if (!readtrail)
#endif
			((P1 *)this)->_5_signal = 0;
		/* merge: printf('Controller: Received status update\\n')(0, 15, 17) */
		reached[1][15] = 1;
		Printf("Controller: Received status update\n");
		/* merge: .(goto)(0, 18, 17) */
		reached[1][18] = 1;
		;
		_m = 4; goto P999; /* 2 */
	case 17: /* STATE 20 - smart_lighting.pml:69 - [-end-] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][20] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* PROC Sensor */
	case 18: /* STATE 1 - smart_lighting.pml:18 - [(!(personPresent))] (4:0:1 - 1) */
		IfNotBlocked
		reached[0][1] = 1;
		if (!( !(((int)now.personPresent))))
			continue;
		/* merge: personPresent = 1(4, 2, 4) */
		reached[0][2] = 1;
		(trpt+1)->bup.oval = ((int)now.personPresent);
		now.personPresent = 1;
#ifdef VAR_RANGES
		logval("personPresent", ((int)now.personPresent));
#endif
		;
		/* merge: printf('Sensor: Person detected\\n')(4, 3, 4) */
		reached[0][3] = 1;
		Printf("Sensor: Person detected\n");
		_m = 3; goto P999; /* 2 */
	case 19: /* STATE 4 - smart_lighting.pml:21 - [sensor_to_controller!personDetected] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][4] = 1;
		if (q_len(now.sensor_to_controller))
			continue;
#ifdef HAS_CODE
		if (readtrail && gui) {
			char simtmp[32];
			sprintf(simvals, "%d!", now.sensor_to_controller);
		sprintf(simtmp, "%d", 4); strcat(simvals, simtmp);		}
#endif
		
		qsend(now.sensor_to_controller, 0, 4, 1);
		{ boq = now.sensor_to_controller; };
		_m = 2; goto P999; /* 0 */
	case 20: /* STATE 5 - smart_lighting.pml:22 - [controller_to_sensor?acknowledgeSensor] (22:0:0 - 1) */
		reached[0][5] = 1;
		if (boq != now.controller_to_sensor) continue;
		if (q_len(now.controller_to_sensor) == 0) continue;

		XX=1;
		if (2 != qrecv(now.controller_to_sensor, 0, 0, 0)) continue;
		if (q_flds[((Q0 *)qptr(now.controller_to_sensor-1))->_t] != 1)
			Uerror("wrong nr of msg fields in rcv");
		;
		qrecv(now.controller_to_sensor, XX-1, 0, 1);
		
#ifdef HAS_CODE
		if (readtrail && gui) {
			char simtmp[32];
			sprintf(simvals, "%d?", now.controller_to_sensor);
		sprintf(simtmp, "%d", 2); strcat(simvals, simtmp);		}
#endif
		if (q_zero(now.controller_to_sensor))
		{	boq = -1;
#ifndef NOFAIR
			if (fairness
			&& !(trpt->o_pm&32)
			&& (now._a_t&2)
			&&  now._cnt[now._a_t&1] == II+2)
			{	now._cnt[now._a_t&1] -= 1;
#ifdef VERI
				if (II == 1)
					now._cnt[now._a_t&1] = 1;
#endif
#ifdef DEBUG
			printf("%3d: proc %d fairness ", depth, II);
			printf("Rule 2: --cnt to %d (%d)\n",
				now._cnt[now._a_t&1], now._a_t);
#endif
				trpt->o_pm |= (32|64);
			}
#endif

		};
		/* merge: printf('Sensor: Received acknowledgment\\n')(0, 6, 22) */
		reached[0][6] = 1;
		Printf("Sensor: Received acknowledgment\n");
		/* merge: .(goto)(0, 23, 22) */
		reached[0][23] = 1;
		;
		_m = 4; goto P999; /* 2 */
	case 21: /* STATE 8 - smart_lighting.pml:27 - [(personPresent)] (10:0:0 - 1) */
		IfNotBlocked
		reached[0][8] = 1;
		if (!(((int)now.personPresent)))
			continue;
		/* merge: printf('Sensor: Room empty\\n')(0, 9, 10) */
		reached[0][9] = 1;
		Printf("Sensor: Room empty\n");
		_m = 3; goto P999; /* 1 */
	case 22: /* STATE 10 - smart_lighting.pml:29 - [sensor_to_controller!roomEmpty] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][10] = 1;
		if (q_len(now.sensor_to_controller))
			continue;
#ifdef HAS_CODE
		if (readtrail && gui) {
			char simtmp[32];
			sprintf(simvals, "%d!", now.sensor_to_controller);
		sprintf(simtmp, "%d", 3); strcat(simvals, simtmp);		}
#endif
		
		qsend(now.sensor_to_controller, 0, 3, 1);
		{ boq = now.sensor_to_controller; };
		_m = 2; goto P999; /* 0 */
	case 23: /* STATE 11 - smart_lighting.pml:30 - [personPresent = 0] (0:0:1 - 1) */
		IfNotBlocked
		reached[0][11] = 1;
		(trpt+1)->bup.oval = ((int)now.personPresent);
		now.personPresent = 0;
#ifdef VAR_RANGES
		logval("personPresent", ((int)now.personPresent));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 24: /* STATE 13 - smart_lighting.pml:34 - [controller_to_sensor?requestStatus] (19:0:0 - 1) */
		reached[0][13] = 1;
		if (boq != now.controller_to_sensor) continue;
		if (q_len(now.controller_to_sensor) == 0) continue;

		XX=1;
		if (1 != qrecv(now.controller_to_sensor, 0, 0, 0)) continue;
		if (q_flds[((Q0 *)qptr(now.controller_to_sensor-1))->_t] != 1)
			Uerror("wrong nr of msg fields in rcv");
		;
		qrecv(now.controller_to_sensor, XX-1, 0, 1);
		
#ifdef HAS_CODE
		if (readtrail && gui) {
			char simtmp[32];
			sprintf(simvals, "%d?", now.controller_to_sensor);
		sprintf(simtmp, "%d", 1); strcat(simvals, simtmp);		}
#endif
		if (q_zero(now.controller_to_sensor))
		{	boq = -1;
#ifndef NOFAIR
			if (fairness
			&& !(trpt->o_pm&32)
			&& (now._a_t&2)
			&&  now._cnt[now._a_t&1] == II+2)
			{	now._cnt[now._a_t&1] -= 1;
#ifdef VERI
				if (II == 1)
					now._cnt[now._a_t&1] = 1;
#endif
#ifdef DEBUG
			printf("%3d: proc %d fairness ", depth, II);
			printf("Rule 2: --cnt to %d (%d)\n",
				now._cnt[now._a_t&1], now._a_t);
#endif
				trpt->o_pm |= (32|64);
			}
#endif

		};
		/* merge: printf('Sensor: Status requested\\n')(0, 14, 19) */
		reached[0][14] = 1;
		Printf("Sensor: Status requested\n");
		_m = 4; goto P999; /* 1 */
	case 25: /* STATE 15 - smart_lighting.pml:37 - [(personPresent)] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][15] = 1;
		if (!(((int)now.personPresent)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 26: /* STATE 16 - smart_lighting.pml:37 - [sensor_to_controller!personDetected] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][16] = 1;
		if (q_len(now.sensor_to_controller))
			continue;
#ifdef HAS_CODE
		if (readtrail && gui) {
			char simtmp[32];
			sprintf(simvals, "%d!", now.sensor_to_controller);
		sprintf(simtmp, "%d", 4); strcat(simvals, simtmp);		}
#endif
		
		qsend(now.sensor_to_controller, 0, 4, 1);
		{ boq = now.sensor_to_controller; };
		_m = 2; goto P999; /* 0 */
	case 27: /* STATE 17 - smart_lighting.pml:38 - [(!(personPresent))] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][17] = 1;
		if (!( !(((int)now.personPresent))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 28: /* STATE 18 - smart_lighting.pml:38 - [sensor_to_controller!roomEmpty] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][18] = 1;
		if (q_len(now.sensor_to_controller))
			continue;
#ifdef HAS_CODE
		if (readtrail && gui) {
			char simtmp[32];
			sprintf(simvals, "%d!", now.sensor_to_controller);
		sprintf(simtmp, "%d", 3); strcat(simvals, simtmp);		}
#endif
		
		qsend(now.sensor_to_controller, 0, 3, 1);
		{ boq = now.sensor_to_controller; };
		_m = 2; goto P999; /* 0 */
	case 29: /* STATE 25 - smart_lighting.pml:42 - [-end-] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][25] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */
	case  _T5:	/* np_ */
		if (!((!(trpt->o_pm&4) && !(trpt->tau&128))))
			continue;
		/* else fall through */
	case  _T2:	/* true */
		_m = 3; goto P999;
#undef rand
	}

