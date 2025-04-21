GRAPHICS=graphics
ASSET_DIR=assets/aseprite
ASEPRITE=~/programs/aseprite/current

ASEPRITES := $(wildcard $(GRAPHICS)/*.aseprite)
ASSETS    := $(ASEPRITES:$(GRAPHICS)/%.aseprite=$(ASSET_DIR)/%.png)
OBJECTS  := $(SOURCES:$(SRCDIR)/%.c=$(OBJDIR)/%.o)

all:$(ASSETS)

$(ASSETS): $(ASSET_DIR)/%.png : $(GRAPHICS)/%.aseprite
	@$(ASEPRITE) --batch --format json-hash --list-slices --list-tags --sheet-pack --sheet $(basename $@).png --data $(basename $@).json $<
