tech_name         ?= sky130
CONFIG            ?= TiniestRocketConfig
ENV_YML           ?= bwrc-env.yml
COMMON_CONF       ?= nov22.yml
BINARY            ?= ${RISCV}/riscv64-unknown-elf/share/riscv-tests/isa/rv32ui-p-simple

ifeq ($(tools), )
    HAMMER_EXEC       = ./nov22
    INPUT_CONFS       ?= $(COMMON_CONF) nov22-commercial.yml $(if $(filter $(VLSI_TOP),Rocket), example-designs/sky130-rocket.yml, )
    VLSI_OBJ_DIR      ?= build-sky130-commercial
    HAMMER_EXTRA_ARGS ?= -p nov22.yml -p nov22-commercial.yml
endif

ifeq ($(tools), openroad)
    HAMMER_EXEC       = ./nov22-openroad
    INPUT_CONFS       = $(COMMON_CONF) nov22-openroad.yml $(if $(filter $(VLSI_TOP),Rocket), example-designs/sky130-rocket.yml, )
    VLSI_OBJ_DIR      = build-sky130-openroad
    HAMMER_EXTRA_ARGS = -p nov22.yml -p nov22-openroad.yml
endif