ifeq ($(tools), openroad)
	tech_name         ?= sky130
	CONFIG            ?= TiniestRocketConfig
	HAMMER_EXEC	      ?= ./nov22-openroad
	ENV_YML           ?= bwrc-env.yml
	INPUT_CONFS       ?= nov22-openroad.yml $(if $(filter $(VLSI_TOP),Rocket), example-designs/sky130-rocket.yml, )
	VLSI_OBJ_DIR      ?= build-sky130-openroad
	HAMMER_EXTRA_ARGS ?= -p nov22-openroad.yml
endif